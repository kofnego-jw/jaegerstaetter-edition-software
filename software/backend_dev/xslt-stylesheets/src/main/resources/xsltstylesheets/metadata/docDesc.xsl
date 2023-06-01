<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei"
    version="2.0">
    
    <xsl:param name="filename"></xsl:param>
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:variable name="document">
        <xsl:copy-of select="/"></xsl:copy-of>
    </xsl:variable>

    <xsl:template match="/">
        <DocDescResult>
            <filename>
                <xsl:value-of select="$filename"/>
            </filename>
            <xsl:for-each select="distinct-values(/tei:TEI//tei:*/name())">
                <xsl:sort/>
                <xsl:variable name="eleName" select="."/>
                <allElementDescs>
                    <elementName>
                        <xsl:value-of select="$eleName"/>
                    </elementName>
                    <xsl:if test="$document//tei:*[name()=$eleName and not(@*)]">
                        <attributeDescList>
                            <attributeName>NONE</attributeName>
                            <attributeValues>NONE</attributeValues>
                        </attributeDescList>
                    </xsl:if>
                    <xsl:for-each select="distinct-values($document//@*[$eleName = parent::tei:*/name()]/name())">
                        <xsl:sort/>
                        <xsl:variable name="attrName" select="."/>
                        <attributeDescList>
                            <attributeName>
                                <xsl:value-of select="$attrName"/>
                            </attributeName>
                            <xsl:for-each select="distinct-values($document//@*[$attrName = name() and $eleName = parent::tei:*/name()]/string())">
                                <xsl:sort/>
                                <attributeValues>
                                    <xsl:value-of select="."/>
                                </attributeValues>
                            </xsl:for-each>
                        </attributeDescList>
                    </xsl:for-each>
                </allElementDescs>                
            </xsl:for-each>
            
            <xsl:for-each select="distinct-values(//tei:div[@type='diplomatische_Umschrift']//tei:*/name())">
                <xsl:sort/>
                <xsl:variable name="eleName" select="."/>
                <diplElementDescs>
                    <elementName>
                        <xsl:value-of select="$eleName"/>
                    </elementName>
                    <xsl:if test="$document//tei:div[@type='diplomatische_Umschrift']//tei:*[name()=$eleName and not(@*)]">
                        <attributeDescList>
                            <attributeName>NONE</attributeName>
                            <attributeValues/>
                        </attributeDescList>
                    </xsl:if>
                    <xsl:for-each select="distinct-values($document//tei:div[@type='diplomatische_Umschrift']//@*[$eleName = parent::tei:*/name()]/name())">
                        <xsl:sort/>
                        <xsl:variable name="attrName" select="."/>
                        <attributeDescList>
                            <attributeName>
                                <xsl:value-of select="$attrName"/>
                            </attributeName>
                            <xsl:for-each select="distinct-values($document//tei:div[@type='diplomatische_Umschrift']//@*[$attrName = name() and $eleName = parent::tei:*/name()]/string())">
                                <xsl:sort/>
                                <attributeValues>
                                    <xsl:value-of select="."/>
                                </attributeValues>
                            </xsl:for-each>
                        </attributeDescList>
                    </xsl:for-each>
                </diplElementDescs>                
            </xsl:for-each>

            <xsl:for-each select="distinct-values(//tei:div[@type='Lesefassung']//tei:*/name())">
                <xsl:sort/>
                <xsl:variable name="eleName" select="."/>
                <normElementDescs>
                    <elementName>
                        <xsl:value-of select="$eleName"/>
                    </elementName>
                    <xsl:if test="$document//tei:div[@type='Lesefassung']//tei:*[name()=$eleName and not(@*)]">
                        <attributeDescList>
                            <attributeName>NONE</attributeName>
                            <attributeValues/>
                        </attributeDescList>
                    </xsl:if>
                    <xsl:for-each select="distinct-values($document//tei:div[@type='Lesefassung']//@*[$eleName = parent::tei:*/name()]/name())">
                        <xsl:sort/>
                        <xsl:variable name="attrName" select="."/>
                        <attributeDescList>
                            <attributeName>
                                <xsl:value-of select="$attrName"/>
                            </attributeName>
                            <xsl:for-each select="distinct-values($document//tei:div[@type='Lesefassung']//@*[$attrName = name() and $eleName = parent::tei:*/name()]/string())">
                                <xsl:sort/>
                                <attributeValues>
                                    <xsl:value-of select="."/>
                                </attributeValues>
                            </xsl:for-each>
                        </attributeDescList>
                    </xsl:for-each>
                </normElementDescs>                
            </xsl:for-each>
            
            <xsl:for-each select="//@facs | //@target">
                <xsl:sort/>
                <refTargets>
                    <xsl:value-of select="."/>
                </refTargets>
            </xsl:for-each>

            <rsPersonCount>
                <xsl:value-of select="count(//tei:rs[@type='person'])"/>
            </rsPersonCount>
            <rsPlaceCount>
                <xsl:value-of select="count(//tei:rs[@type='place'])"/>
            </rsPlaceCount>
            <rsCorpCount>
                <xsl:value-of select="count(//tei:rs[@type='org'])"/>
            </rsCorpCount>
            <footnoteCount>
                <xsl:value-of select="count(//tei:note[@type='footnote'])"/>
            </footnoteCount>
        </DocDescResult>
    </xsl:template>
    
</xsl:stylesheet>