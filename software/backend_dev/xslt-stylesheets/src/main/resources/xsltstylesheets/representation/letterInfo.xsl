<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei" version="2.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="filename"/>

    <xsl:template name="titleStmt">
        <title>
            <xsl:value-of select="//tei:titleStmt/tei:title"/>
        </title>
        <author>
            <xsl:value-of select="//tei:titleStmt/tei:author"/>
        </author>
    </xsl:template>

    <xsl:template name="sourceDesc">
        <msIdentifier>
            <institution>
                <xsl:value-of select="//tei:msIdentifier/tei:institution"/>
            </institution>
            <repository>
                <xsl:value-of select="//tei:msIdentifier/tei:repository"/>
            </repository>
            <collection>
                <xsl:value-of select="//tei:msIdentifier/tei:collection"/>
            </collection>
            <idno>
                <xsl:value-of select="//tei:msIdentifier/tei:idno"/>
            </idno>
            <altIdno>
                <xsl:value-of select="//tei:msIdentifier/tei:altIdentifier/tei:idno"/>
            </altIdno>
        </msIdentifier>
        <msContents>
            <xsl:value-of select="//tei:msContents/normalize-space()"/>
        </msContents>
        <physDesc>
            <xsl:apply-templates select="//tei:physDesc"/>
        </physDesc>
        <objectType>
            <xsl:apply-templates select="//tei:physDesc//tei:objectType"/>
        </objectType>
        <origin>
            <xsl:value-of select="//tei:origin/normalize-space()"/>
        </origin>
        <provenance>
            <xsl:value-of select="//tei:provenance/normalize-space()"/>
        </provenance>
    </xsl:template>

    <xsl:template match="tei:objectType">
        <xsl:text> (</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>) </xsl:text>
    </xsl:template>

    <xsl:template match="tei:correspAction">
        <personList>
            <xsl:for-each select=".//tei:rs[@type = 'person']">
                <personList>
                    <name>
                        <xsl:value-of select="normalize-space()"/>
                    </name>
                    <ref>
                        <xsl:value-of select="@ref"/>
                    </ref>
                    <key>
                        <xsl:value-of select="@key"/>
                    </key>
                </personList>
            </xsl:for-each>
        </personList>
        <placeList>
            <xsl:for-each select=".//tei:rs[@type = 'place']">
                <placeList>
                    <name>
                        <xsl:value-of select="normalize-space()"/>
                    </name>
                    <key>
                        <xsl:value-of select="@key"/>
                    </key>
                </placeList>
            </xsl:for-each>
        </placeList>
        <date>
            <xsl:value-of select=".//tei:date/@when"/>
        </date>
    </xsl:template>

    <xsl:template name="correspDesc">
        <sentAction>
            <xsl:apply-templates select="//tei:correspAction[@type = 'sent']"/>
        </sentAction>
        <receivedAction>
            <xsl:apply-templates select="//tei:correspAction[@type = 'received']"/>
        </receivedAction>
        <correspContext>
            <prevLetterFiles>
                <xsl:variable name="count" select="count(//tei:correspContext/tei:ref[@type='prev'])"/>
                <xsl:choose>
                    <xsl:when test="$count = 1">
                        <xsl:for-each
                            select="tokenize(normalize-space(//tei:correspContext/tei:ref[@type = 'prev']/@target), '\s*;\s*')">
                            <prevLetterFiles>
                                <xsl:value-of select="."/>
                            </prevLetterFiles>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$count &gt; 1">
                        <xsl:for-each
                            select="//tei:correspContext/tei:ref[@type = 'prev']/@target">
                            <prevLetterFiles>
                                <xsl:value-of select="."/>
                            </prevLetterFiles>
                        </xsl:for-each>
                    </xsl:when>
                </xsl:choose>
            </prevLetterFiles>
            <nextLetterFiles>
                <xsl:variable name="count" select="count(//tei:correspContext/tei:ref[@type='next'])"/>
                <xsl:choose>
                    <xsl:when test="$count = 1">
                        <xsl:for-each
                            select="tokenize(normalize-space(//tei:correspContext/tei:ref[@type = 'next']/@target), '\s*;\s*')">
                            <nextLetterFiles>
                                <xsl:value-of select="."/>
                            </nextLetterFiles>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$count &gt; 1">
                        <xsl:for-each
                            select="//tei:correspContext/tei:ref[@type = 'next']/@target">
                            <nextLetterFiles>
                                <xsl:value-of select="."/>
                            </nextLetterFiles>
                        </xsl:for-each>
                    </xsl:when>
                </xsl:choose>
            </nextLetterFiles>
        </correspContext>

    </xsl:template>

    <xsl:template match="tei:note">
        <!-- wird übersprungen -->
    </xsl:template>

    <xsl:template match="/">
        <LetterInfo>
            <filename>
                <xsl:value-of select="$filename"/>
            </filename>
            <titleStmt>
                <xsl:call-template name="titleStmt"/>
            </titleStmt>
            <sourceDesc>
                <xsl:call-template name="sourceDesc"/>
            </sourceDesc>
            <correspDesc>
                <xsl:call-template name="correspDesc"/>
            </correspDesc>
            <diplo>
                <xsl:apply-templates select="//tei:body/tei:div[@type = 'diplomatische_Umschrift']"
                />
            </diplo>
            <norm>
                <xsl:apply-templates select="//tei:body/tei:div[@type = 'Lesefassung']"/>
            </norm>
        </LetterInfo>
    </xsl:template>

</xsl:stylesheet>