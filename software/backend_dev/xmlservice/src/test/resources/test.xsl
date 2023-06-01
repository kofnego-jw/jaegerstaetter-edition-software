<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns="http://www.w3.org/1999/xhtml"
                version="2.0">
    <xsl:output method="xml" indent="no"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:value-of select="//tei:titleStmt/tei:title"/>
                </title>
            </head>
            <body>
                <xsl:apply-templates select="//tei:text"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="tei:div">
        <div>
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:p">
        <p>
            <xsl:apply-templates/>
        </p>
    </xsl:template>

    <xsl:template match="tei:hi">
        <span>
            <xsl:choose>
                <xsl:when test="@rend = 'italic'">
                    <xsl:attribute name="style">font-variant: italic;</xsl:attribute>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates/>
        </span>
    </xsl:template>

</xsl:stylesheet>
