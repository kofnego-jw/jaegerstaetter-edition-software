<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="xs tei"
    version="2.0">
    
    <xsl:output method="xml" omit-xml-declaration="yes" indent="no"/>
    
    <xsl:param name="type">person</xsl:param>
    <xsl:param name="key">P_0001</xsl:param>
    
    <xsl:template match="/">
        <div>
            <xsl:apply-templates select="//tei:div[@type=$type and @key=$key]"/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:head">
        <h4>
            <xsl:apply-templates/>
        </h4>
    </xsl:template>
    
    <xsl:template match="tei:table">
        <table>
            <xsl:apply-templates/>
        </table>
    </xsl:template>
    
    <xsl:template match="tei:row">
        <tr>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>
    
    <xsl:template match="tei:cell">
        <td>
            <xsl:apply-templates/>
        </td>
    </xsl:template>
    
    <xsl:template match="tei:list">
        <ul style="list-style:none">
            <xsl:apply-templates/>
        </ul>
    </xsl:template>
    
    <xsl:template match="tei:item">
        <li>
            <xsl:apply-templates/>
        </li>
    </xsl:template>
    
    <xsl:template match="tei:ref">
        <a>
            <xsl:if test="@target">
                <xsl:attribute name="target">_blank</xsl:attribute>
                <xsl:attribute name="href" select="@target"/>
            </xsl:if>
            <xsl:apply-templates/>
        </a>
    </xsl:template>
</xsl:stylesheet>