<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
    <xsl:output method="xml"/>
    
    <xsl:template match="/">
        <test>
            <xsl:copy-of select="parse-xml-fragment('test &lt;hello&gt;blah&lt;/hello&gt;> test')"></xsl:copy-of>
        </test>
    </xsl:template>
    
</xsl:stylesheet>