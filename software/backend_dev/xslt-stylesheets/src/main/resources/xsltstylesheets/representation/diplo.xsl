<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="xs tei"
                version="2.0">

    <xsl:include href="shared.xsl"/>
    
    <xsl:variable name="displayMode">dipl</xsl:variable>

    <xsl:output method="html" indent="no"/>

    <xsl:template match="/">
        <div class="tei">
            <h2>Diplomatische Umschrift</h2>
            <div class="text">
                <xsl:apply-templates select="//tei:div[@type='diplomatische_Umschrift']"/>
            </div>
        </div>
    </xsl:template>


</xsl:stylesheet>
