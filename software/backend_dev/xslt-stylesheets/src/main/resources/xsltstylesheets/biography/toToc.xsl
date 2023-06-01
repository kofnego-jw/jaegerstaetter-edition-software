<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs" version="2.0">

    <xsl:include href="../ffji-helper.xsl"/>

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//tei:body"/>
    </xsl:template>

    <xsl:template match="tei:body">
        <ul>
            <xsl:apply-templates select="tei:div[tei:head]"/>
        </ul>

    </xsl:template>

    <xsl:template match="tei:div[tei:head]">
        <li>
            <xsl:apply-templates select="tei:head"/>
            <xsl:if test="tei:div[tei:head]">
                <ul>
                    <xsl:apply-templates select="tei:div[tei:head]"/>
                </ul>
            </xsl:if>
        </li>
    </xsl:template>

    <xsl:template match="tei:head">
        <xsl:call-template name="createScrollButton">
            <xsl:with-param name="aimId">
                <xsl:text>head</xsl:text>
                <xsl:value-of select="count(preceding::tei:head) + 1"/>
            </xsl:with-param>
            <xsl:with-param name="content" select="normalize-space()"/>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>