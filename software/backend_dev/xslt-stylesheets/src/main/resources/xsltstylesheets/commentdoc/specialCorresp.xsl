<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
    exclude-result-prefixes="xs tei uibk" version="2.0">
    
    <xsl:output method="html" indent="no"/>
    
    <xsl:include href="../ffji-helper.xsl"/>
    
    <xsl:template match="/">
        <div class="special-corresp-index">
            <div class="bio-index-left">
                <h1>
                    <xsl:apply-templates select="//tei:title"/>
                </h1>
                <xsl:apply-templates select="//tei:body"/>
            </div>
            <div class="bio-index-right">
                <!-- -->
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:div">
        <ffji-div>
            <xsl:if test="tei:head">
                <xsl:attribute name="highlightid">
                    <xsl:text>head</xsl:text>
                    <xsl:value-of select="count(preceding::tei:head) + 1"/>
                </xsl:attribute>
            </xsl:if>
            <div>
                <xsl:if test="@rend | @type">
                    <xsl:attribute name="class">
                        <xsl:value-of select="@rend"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="@type"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:apply-templates/>
            </div>
        </ffji-div>
    </xsl:template>
    
    <xsl:template match="tei:body/tei:head">
        <!-- Ignore this -->
    </xsl:template>
    
    <xsl:template match="tei:head">
        <xsl:variable name="level" select="count(ancestor::tei:div)"/>
        <xsl:variable name="headId" select="generate-id()"/>
        <xsl:choose>
            <xsl:when test="$level = 1">
                <h2>
                    <xsl:call-template name="createScrollAnchor">
                        <xsl:with-param name="id">
                            <xsl:text>head</xsl:text>
                            <xsl:value-of select="count(preceding::tei:head) + 1"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:apply-templates/>
                </h2>
            </xsl:when>
            <xsl:when test="$level = 2">
                <h3>
                    <xsl:call-template name="createScrollAnchor">
                        <xsl:with-param name="id">
                            <xsl:text>head</xsl:text>
                            <xsl:value-of select="count(preceding::tei:head) + 1"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:apply-templates/>
                </h3>
            </xsl:when>
            <xsl:when test="$level = 3">
                <h4>
                    <xsl:call-template name="createScrollAnchor">
                        <xsl:with-param name="id">
                            <xsl:text>head</xsl:text>
                            <xsl:value-of select="count(preceding::tei:head) + 1"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:apply-templates/>
                </h4>
            </xsl:when>
            <xsl:otherwise>
                <h5>
                    <xsl:call-template name="createScrollAnchor">
                        <xsl:with-param name="id">
                            <xsl:text>head</xsl:text>
                            <xsl:value-of select="count(preceding::tei:head) + 1"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:apply-templates/>
                </h5>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:lb">
        <br/>
    </xsl:template>
    
    <xsl:template match="tei:p">
        <p>
            <xsl:if test="@rend">
                <xsl:attribute name="class">
                    <xsl:value-of select="@rend"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </p>
    </xsl:template>
    
    <xsl:template match="tei:list">
        <xsl:choose>
            <xsl:when test="@type = 'ordered'">
                <ol>
                    <xsl:apply-templates/>
                </ol>
            </xsl:when>
            <xsl:otherwise>
                <ul>
                    <xsl:apply-templates/>
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:list/tei:item">
        <li>
            <xsl:apply-templates/>
        </li>
    </xsl:template>
    
    <xsl:template match="tei:hi">
        <span>
            <xsl:if test="@rend">
                <xsl:attribute name="class" select="@rend"/>
            </xsl:if>
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:figure">
        <ffji-bioindex-figure class="bio-figure">
            <xsl:if test="tei:graphic/@url">
                <xsl:attribute name="src" select="tei:graphic/@url"/>                
            </xsl:if>
            <xsl:if test="tei:figDesc">
                <xsl:attribute name="desc" select="normalize-space(tei:figDesc)"/>
            </xsl:if>
        </ffji-bioindex-figure>
    </xsl:template>
    
    <xsl:template match="tei:ref">
        <xsl:choose>
            <xsl:when test="@type='specialCorresp'">
                <ffji-special-corresp>
                    <xsl:attribute name="partners1">
                        <xsl:for-each select="ancestor::tei:div[1]//tei:rs[@type='person' and @role='partner1']">
                            <xsl:choose>
                                <xsl:when test="@key">
                                    <xsl:value-of select="uibk:keytoname(@key, 'person', 'true')"/>
                                </xsl:when>
                                <xsl:when test="@ref">
                                    <xsl:variable name="key">
                                        <xsl:choose>
                                            <xsl:when test="contains(@ref, '#')">
                                                <xsl:value-of select="substring-after(@ref, '#')"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="@ref"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <xsl:value-of select="uibk:keytoname($key, 'person', 'true')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>UNBEKANNT</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="position()!=last()">
                                <xsl:text>;</xsl:text>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:attribute>
                    <xsl:attribute name="partners2">
                        <xsl:for-each select="ancestor::tei:div[1]//tei:rs[@type='person' and @role='partner2']">
                            <xsl:choose>
                                <xsl:when test="@key">
                                    <xsl:value-of select="uibk:keytoname(@key, 'person', 'true')"/>
                                </xsl:when>
                                <xsl:when test="@ref">
                                    <xsl:variable name="key">
                                        <xsl:choose>
                                            <xsl:when test="contains(@ref, '#')">
                                                <xsl:value-of select="substring-after(@ref, '#')"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="@ref"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <xsl:value-of select="uibk:keytoname($key, 'person', 'true')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>UNBEKANNT</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="position()!=last()">
                                <xsl:text>;</xsl:text>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:attribute>
                    <xsl:apply-templates/>
                </ffji-special-corresp>
            </xsl:when>
            <xsl:otherwise>
                <ffji-commentdoc-ref>
                    <xsl:variable name="target">
                        <xsl:choose>
                            <xsl:when test="starts-with(@target, 'http://') or starts-with(@target, 'https://')">
                                <xsl:value-of select="@target"/>
                            </xsl:when>
                            <xsl:when test="ends-with(@target, '.xml')">
                                <xsl:value-of select="normalize-space(@target)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="@target"/>
                                <xsl:text>.xml</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:attribute name="target" select="$target"/>
                    <xsl:apply-templates/>
                </ffji-commentdoc-ref>
            </xsl:otherwise>
        </xsl:choose>
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
        <xsl:choose>
            <xsl:when test="@role='label'">
                <th>
                    <xsl:if test="@rows">
                        <xsl:attribute name="rowspan" select="@rows"/>
                    </xsl:if>
                    <xsl:if test="@cols">
                        <xsl:attribute name="colspan" select="@cols"/>
                    </xsl:if>
                    <xsl:apply-templates/>
                </th>
            </xsl:when>
            <xsl:otherwise>
                <td>
                    <xsl:if test="@rows">
                        <xsl:attribute name="rowspan" select="@rows"/>
                    </xsl:if>
                    <xsl:if test="@cols">
                        <xsl:attribute name="colspan" select="@cols"/>
                    </xsl:if>
                    <xsl:apply-templates/>
                </td>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>