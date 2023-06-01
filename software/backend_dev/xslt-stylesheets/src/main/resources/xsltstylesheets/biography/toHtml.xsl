<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei" version="2.0">

    <xsl:output method="html" omit-xml-declaration="yes" indent="no"/>

    <xsl:include href="../ffji-helper.xsl"/>

    <xsl:template match="/">
        <div class="biography">
            <h1>
                <xsl:apply-templates select="//tei:title"/>
            </h1>
            <xsl:if test="//tei:titleStmt//tei:author">
                <div class="author">
                    <p>Verfasst von <xsl:value-of select="string-join(//tei:titleStmt//tei:author, ';')"/>
                    </p>
                </div>
            </xsl:if>
            <div class="biographyText">
                <xsl:apply-templates select="//tei:body"/>
            </div>
            <xsl:if test="//tei:body//tei:note">
                <div class="biographyEndnote">
                    <h2>Anmerkungen</h2>
                    <xsl:apply-templates select="//tei:body//tei:note" mode="endnote" />
                </div>                
            </xsl:if>
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
        <xsl:if test="parent::tei:body and following-sibling::tei:div">
            <hr class="separator"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="tei:body/tei:head">
        <!-- Ignore this -->
    </xsl:template>

    <xsl:template match="tei:head">
        <xsl:variable name="level" select="count(ancestor::tei:div)"/>
        <xsl:variable name="headId" select="generate-id()"/>
        <xsl:choose>
            <xsl:when test="$level = 1 or $level = 0">
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
        <xsl:choose>
            <xsl:when test="@rend='endnote_reference' and count(child::tei:*[name()!='note'])=0">
                <span><xsl:apply-templates select="tei:note"/></span>
            </xsl:when>
            <xsl:otherwise>
                <span>
                    <xsl:if test="@rend or @rendition">
                        <xsl:attribute name="class">
                            <xsl:value-of select="@rend"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="@rendition"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:apply-templates/>
                </span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:note">
        <xsl:variable name="counter" select="count(preceding::tei:note[ancestor::tei:body]) + 1"/>
        <xsl:variable name="anchorId" select="concat('fn', $counter, '_text')"/>
        <xsl:variable name="destId" select="concat('fn', $counter, '_footnote')"/>
        <ffji-bio-note>
            <xsl:attribute name="n" select="$counter"/>
            <xsl:attribute name="dest" select="$destId"/>
        </ffji-bio-note>
        <ffji-anchor>
            <xsl:attribute name="id" select="$anchorId"/>
        </ffji-anchor>
    </xsl:template>
    
    <xsl:template match="tei:note" mode="endnote">
        <xsl:variable name="counter" select="count(preceding::tei:note[ancestor::tei:body]) + 1"/>
        <xsl:variable name="destId" select="concat('fn', $counter, '_text')"/>
        <xsl:variable name="anchorId" select="concat('fn', $counter, '_footnote')"/>
        <div class="biography_footnote">
            <div class="counter">
                <ffji-anchor>
                    <xsl:attribute name="id" select="$anchorId"/>
                </ffji-anchor>
                <ffji-bio-note>
                    <xsl:attribute name="n" select="$counter"/>
                    <xsl:attribute name="dest" select="$destId"/>
                </ffji-bio-note>
            </div>
            <div class="footnote_content">
                <xsl:apply-templates/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:figure">
        <ffji-bio-figure>
            <xsl:if test="tei:graphic/@url">
                <xsl:attribute name="src" select="tei:graphic/@url"/>                
            </xsl:if>
            <xsl:if test="tei:figDesc">
                <xsl:attribute name="desc" select="normalize-space(tei:figDesc)"/>
            </xsl:if>
        </ffji-bio-figure>
    </xsl:template>
    
    <xsl:template match="tei:ref">
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
    </xsl:template>
    
</xsl:stylesheet>