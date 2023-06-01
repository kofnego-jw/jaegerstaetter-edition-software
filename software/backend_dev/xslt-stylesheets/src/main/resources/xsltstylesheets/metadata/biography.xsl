<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
                exclude-result-prefixes="tei uibk"
                version="2.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="fileName">Bio_JK.xml</xsl:param>

    <xsl:template match="/">
        <BiographyResult>
            <filename>
                <xsl:value-of select="$fileName"/>
            </filename>
            <title>
                <xsl:call-template name="createTitle"/>
            </title>
            <xsl:for-each select="//tei:titleStmt//tei:rs[@type='person']">
                <xsl:choose>
                    <xsl:when test="@key">
                        <persNameKeys>
                            <xsl:value-of select="normalize-space(@key)"/>
                        </persNameKeys>
                    </xsl:when>
                    <xsl:when test="@ref">
                        <persNameKeys>
                            <xsl:choose>
                                <xsl:when test="starts-with(normalize-space(@ref), '#')">
                                    <xsl:value-of select="substring(normalize-space(@ref), 2)"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="normalize-space(@ref)"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:value-of select="normalize-space(@key)"/>
                        </persNameKeys>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
        </BiographyResult>
    </xsl:template>

    <xsl:template name="createTitle">
        <xsl:choose>
            <xsl:when test="//tei:body/tei:head and count(//tei:body/tei:head) = 1">
                <xsl:value-of select="normalize-space(//tei:body/tei:head[1])"/>
            </xsl:when>
            <xsl:when test="//tei:body//tei:head">
                <xsl:value-of select="normalize-space((//tei:body//tei:head)[1])"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="normalize-space(//tei:titleStmt/tei:title[1])"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
