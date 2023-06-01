<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei"
    version="2.0">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:include href="../ffji-helper.xsl"/>
    
    <xsl:template match="/">
        <TocList>
            <toc>
                <xsl:for-each select="//tei:body//tei:div[@type='Lesefassung']/tei:div">
                    <toc>
                        <xsl:apply-templates select="."/>
                    </toc>
                </xsl:for-each>
            </toc>
        </TocList>
    </xsl:template>
        
    <xsl:template match="tei:div">
        <xsl:variable name="id">
            <xsl:call-template name="createDivId">
                <xsl:with-param name="div" select="."/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="heading">
            <xsl:choose>
                <xsl:when test="tei:head">
                    <xsl:value-of select="normalize-space(tei:head[1])"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>[Ohne Titel]</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <id>
            <xsl:value-of select="$id"/>
        </id>
        <title>
            <xsl:value-of select="$heading"/>
        </title>
        <children>
            <xsl:for-each select="tei:div">
                <children>
                    <xsl:apply-templates select="."/>
                </children>
            </xsl:for-each>
        </children>
    </xsl:template>
    
</xsl:stylesheet>
