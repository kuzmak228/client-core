package com.kuzmak.client.core.xml;

import com.kuzmak.client.core.exception.ModelMappingException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Xml parser based on {@link XmlDescriptor}.
 */
public class XmlParser {

    private static final DocumentBuilderFactory DOCUMENT_FACTORY = DocumentBuilderFactory.newInstance();

    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();

    private final Map<Class<?>, XmlDescriptor> descriptors = new HashMap<>();

    private Map<Class<?>, Function<String, ?>> converters = new HashMap<>(Map.of(
            String.class, value -> value,
            Boolean.class, Boolean::valueOf,
            Byte.class, Byte::valueOf,
            Short.class, Short::valueOf,
            Integer.class, Integer::valueOf,
            Long.class, Long::valueOf,
            Float.class, Float::valueOf,
            Double.class, Double::valueOf,
            BigInteger.class, BigInteger::new
    ));

    /**
     * Converts string xml to {@link Element}.
     *
     * @param xml string xml
     * @return xml element from string
     * @throws ModelMappingException in case of errors
     */
    public static Element toElement(final String xml) throws ModelMappingException {
        try {
            return DOCUMENT_FACTORY.newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
        } catch (final Exception e) {
            throw new ModelMappingException(e.getMessage(), e);
        }
    }

    /**
     * Sets descriptor for given class.
     *
     * @param clazz class
     * @param descriptor descriptor
     */
    public void setDescriptor(final Class<?> clazz, final XmlDescriptor descriptor) {
        descriptors.put(clazz, descriptor);
    }

    /**
     * Sets converter from {@link String} for given class.
     *
     * @param clazz class
     * @param converter converter
     * @param <T> return type of converter
     */
    public <T> void setConverter(final Class<T> clazz, final Function<String, T> converter) {
        converters.put(clazz, converter);
    }

    /**
     * Gets first of objects of given class or {@code null} if no results.
     *
     * @param fromElement element, from which object will be got
     * @param clazz class
     * @param <T> return type of object
     * @return first object of given class
     * @throws ModelMappingException in case of errors
     */
    public <T> T getFirst(final Element fromElement, final Class<T> clazz) throws ModelMappingException {
        final var all = getAll(fromElement, clazz);
        return all.isEmpty() ? null : all.get(0);
    }

    /**
     * Gets list of objects of given class.
     *
     * @param fromElement element, from which list will be got
     * @param clazz class
     * @param <T> return type of list
     * @return list of objects of given class
     * @throws ModelMappingException in case of errors
     */
    public <T> List<T> getAll(final Element fromElement, final Class<T> clazz) throws ModelMappingException {
        if (!descriptors.containsKey(clazz)) {
            throw new ModelMappingException("No descriptor for: " + clazz.getName());
        }

        try {
            return convert((NodeList) X_PATH_FACTORY.newXPath().evaluate(descriptors.get(clazz).elementXpath(), fromElement,
                    XPathConstants.NODESET), clazz);
        } catch (final Exception e) {
            throw new ModelMappingException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convert(final Node node, final Class<T> clazz) throws ModelMappingException {
        if (converters.containsKey(clazz)) {
            final var converter = converters.get(clazz);
            return node == null || StringUtils.isEmpty(node.getTextContent()) ? null :
                    (T) converter.apply(node.getTextContent());
        }

        if (!descriptors.containsKey(clazz)) {
            throw new ModelMappingException("No converter and descriptor for: " + clazz.getName());
        }

        final var descriptor = descriptors.get(clazz);

        try {
            final T object = clazz.getDeclaredConstructor().newInstance();

            for (final var field : clazz.getDeclaredFields()) {
                if (!descriptor.ignoredFields().contains(field.getName())) {
                    field.setAccessible(true);

                    final var xpathExpression = descriptor.fieldsXpath().containsKey(field.getName()) ?
                            descriptor.fieldsXpath().get(field.getName()) : "./" + field.getName();

                    if (List.class.isAssignableFrom(field.getType())) {
                        final var genericClazz = (Class<?>) ((ParameterizedType) field.getGenericType())
                                .getActualTypeArguments()[0];
                        field.set(object, convert((NodeList) X_PATH_FACTORY.newXPath().evaluate(xpathExpression, node,
                                XPathConstants.NODESET), genericClazz));
                    } else {
                        field.set(object, convert((Node) X_PATH_FACTORY.newXPath().evaluate(xpathExpression, node,
                                XPathConstants.NODE), field.getType()));
                    }
                }
            }

            return object;
        } catch (final Exception e) {
            throw new ModelMappingException(e.getMessage(), e);
        }
    }

    private <T> List<T> convert(final NodeList nodes, final Class<T> clazz) throws Exception {
        final var result = new ArrayList<T>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(convert(nodes.item(i), clazz));
        }

        return result;
    }
}
