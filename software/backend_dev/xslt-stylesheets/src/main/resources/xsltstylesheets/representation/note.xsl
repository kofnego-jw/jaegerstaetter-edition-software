<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="xs tei"
                version="2.0">

    <xsl:include href="shared.xsl"/>
    <xsl:param name="noteId"/>

    <xsl:variable name="displayMode">note</xsl:variable>

    <xsl:output method="html" indent="no"/>

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="//tei:note[count(preceding::tei:note) + count(ancestor::tei:note) +  1 = number($noteId)]">
                <xsl:variable name="noteNode"
                              select="//tei:note[count(preceding::tei:note) + count(ancestor::tei:note) +  1 = number($noteId)]"/>
                <div class="ffji-note">
                    <xsl:choose>
                        <xsl:when test="$noteNode/@subtype='slang'">
                            <i>
                                <xsl:value-of select="normalize-space($noteNode/preceding::tei:ref[1])"/>
                            </i>
                            <br/>
                            <xsl:apply-templates select="$noteNode/(text()|tei:*)"/>
                        </xsl:when>
                        <xsl:when
                                test="count($noteNode/tei:ref) = 1 and count($noteNode/tei:*) = 1 and count($noteNode/text()[normalize-space()!='']) = 0">
                            <xsl:apply-templates select="$noteNode/tei:ref"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="$noteNode/(text()|tei:*)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <div>
                    <p>Kommentar nicht gefunden.</p>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


</xsl:stylesheet>
