package com.kuzmak.client.core.xml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Xml descriptor. Describes mapping of class in xml
 * Read https://www.w3schools.com/xml/xpath_syntax.asp for syntax info
 *
 */
public interface XmlDescriptor {

    /**
     * Returns xpath expression of element.
     *
     * @return xpath expression of element
     */
    String elementXpath();

    /**
     * Returns xpath expressions for fields relative to element.
     *
     * Note, that if field is not present, field name will be used.
     * For example, for field with name "abracadabra" default xpath expression will be "./abracadabra"
     *
     * @return mapping of fields
     */
    default Map<String, String> fieldsXpath() {
        return Collections.emptyMap();
    }

    /**
     * Returns list of ignored fields.
     *
     * @return list of ignored fields
     */
    default List<String> ignoredFields() {
        return Collections.emptyList();
    }
}
