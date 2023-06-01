<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:fiba="http://www.uibk.ac.at/brenner-archiv/xml/ns"
    xmlns="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei fiba"
    version="2.0">
    
    <xsl:output method="xml" indent="yes" />
    
    <xsl:template match="/">
        <TEI>
            <teiHeader>
                <fileDesc>
                    <titleStmt>
                        <title>Franz und Franziska Jägerstätter Edition, Register</title>
                        <editor>Andreas Schmoller und Verena Lorber für das Franz und Franziska Jägerstätter
                            Institut</editor>
                        <respStmt>
                            <name>Schmoller, Andreas</name>
                            <resp>Auszeichnung, Kommentierung, Erstellung Lesefassung</resp>
                        </respStmt>
                        <respStmt>
                            <name>Lorber, Verena</name>
                            <resp>Korrektur, Kommentierung, Auszeichnung</resp>
                        </respStmt>
                    </titleStmt>
                    <publicationStmt>
                        <authority>Katholische Privatuniversität Linz, Franz und Franziska Jägerstätter
                            Institut</authority>
                        <address>
                            <addrLine>Bethlehemstraße 20</addrLine>
                            <addrLine>4020 Linz</addrLine>
                        </address>
                        <availability>
                            <p>This is an open access work licensed under a creative commons attribution 4.0
                                international license</p>
                        </availability>
                        <ptr target="https://ku-linz.at/forschung/franz_und_franziska_jaegerstaetter_institut/"
                        />
                    </publicationStmt>
                    <sourceDesc>
                        <p>This is an XML file generated from a registry database.</p>
                    </sourceDesc>
                </fileDesc>
            </teiHeader>
            <text>
                <body>
                    <div type="person">
                        <head>Personenverzeichnis</head>
                        <xsl:call-template name="createPersonEntries"/>
                    </div>
                    <div type="place">
                        <head>Ortsverzeichnis</head>
                        <xsl:call-template name="createPlaceEntries"/>
                    </div>
                    <div type="saint">
                        <head>Verzeichnis der Heiligen</head>
                        <xsl:call-template name="createSaintEntries"/>
                    </div>
                    <div type="corporation">
                        <head>Verzeichnis der Körperschaften</head>
                        <xsl:call-template name="createCorporationEntries"/>                        
                    </div>
                </body>
            </text>
        </TEI>
    </xsl:template>
    
    <xsl:function name="fiba:createTitle">
        <xsl:param name="entry"/>
        <xsl:choose>
            <xsl:when test="$entry/preferredName != ''">
                <xsl:value-of select="$entry/preferredName"/>
            </xsl:when>
            <xsl:when test="$entry/surname!='' and $entry/forename!=''">
                <xsl:value-of select="concat($entry/surname, ', ', $entry/forename)"/>
            </xsl:when>
            <xsl:when test="$entry/locationName != ''">
                <xsl:value-of select="$entry/locationName"/>
            </xsl:when>
            <xsl:when test="$entry/title !=''">
                <xsl:value-of select="$entry/title"/>
            </xsl:when>
            <xsl:when test="$entry/organisation !=''">
                <xsl:value-of select="$entry/organisation"/>
            </xsl:when>
            <xsl:when test="$entry/key !=''">
                <xsl:value-of select="$entry/key"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>UNBEKANNT</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:template match="controlledVocabularies/controlledVocabularies">
        <item>
            <ref>
                <xsl:attribute name="target">
                    <xsl:choose>
                        <xsl:when test="authority = 'GND'">
                            <xsl:text>https://d-nb.info/gnd/</xsl:text>
                        </xsl:when>
                        <xsl:when test="authority = 'GEONAMES'">
                            <xsl:text>https://www.geonames.org/</xsl:text>
                        </xsl:when>
                        <xsl:when test="authority = 'WiKIDATA'">
                            <xsl:text>https://www.wikidata.org/wiki/</xsl:text>
                        </xsl:when>
                        <xsl:when test="authority = 'VIAF'">
                            <xsl:text>https://viaf.org/viaf/</xsl:text>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:value-of select="controlledId"/>
                </xsl:attribute>
                <xsl:value-of select="authority"/>
                <xsl:text>: </xsl:text>
                <xsl:value-of select="controlledId"/>
            </ref>
        </item>
    </xsl:template>
    
    <xsl:template name="createPersonEntries">
        <xsl:for-each select="//personInfoList/personInfoList">
            <xsl:sort lang="de" select="fiba:createTitle(.)"/>
            <xsl:variable name="entryTitle" select="fiba:createTitle(.)"/>
            <div>
                <xsl:attribute name="key" select="key"/>
                <xsl:attribute name="type">person</xsl:attribute>
                <head>
                    <xsl:value-of select="$entryTitle"/>
                </head>
                <table>
                    <row>
                        <cell>Nachname</cell>
                        <cell>
                            <xsl:value-of select="surname"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Vorname</cell>
                        <cell>
                            <xsl:value-of select="forename"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Auch bekannt unter</cell>
                        <cell>
                            <xsl:value-of select="string-join(addNames/addNames, ', ')"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Geburtsdatum</cell>
                        <cell>
                            <xsl:value-of select="birthDate"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Geburtsort</cell>
                        <cell>
                            <xsl:value-of select="birthPlace"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Sterbedatum</cell>
                        <cell>
                            <xsl:value-of select="deathDate"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Sterbeort</cell>
                        <cell>
                            <xsl:value-of select="deathPlace"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Anmerkungen</cell>
                        <cell>
                            <xsl:value-of select="note"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Linked Data</cell>
                        <cell>
                            <xsl:choose>
                                <xsl:when test="count(controlledVocabularies/controlledVocabularies) &gt; 0">
                                    <list>
                                        <xsl:for-each select="controlledVocabularies/controlledVocabularies">
                                            <xsl:apply-templates select="."/>
                                        </xsl:for-each>
                                    </list>
                                </xsl:when>
                            </xsl:choose>
                        </cell>
                    </row>
                </table>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="createCorporationEntries">
        <xsl:for-each select="//corporationInfoList/corporationInfoList">
            <xsl:sort lang="de" select="fiba:createTitle(.)"/>
            <xsl:variable name="entryTitle" select="fiba:createTitle(.)"/>
            <div>
                <xsl:attribute name="key" select="key"/>
                <xsl:attribute name="type">corporation</xsl:attribute>
                <head>
                    <xsl:value-of select="$entryTitle"/>
                </head>
                <table>
                    <row>
                        <cell>Name der Organisation</cell>
                        <cell>
                            <xsl:value-of select="organisation"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Anmerkungen</cell>
                        <cell>
                            <xsl:value-of select="note"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Linked Data</cell>
                        <cell>
                            <xsl:choose>
                                <xsl:when test="count(controlledVocabularies/controlledVocabularies) &gt; 0">
                                    <list>
                                        <xsl:for-each select="controlledVocabularies/controlledVocabularies">
                                            <xsl:apply-templates select="."/>
                                        </xsl:for-each>
                                    </list>
                                </xsl:when>
                            </xsl:choose>
                        </cell>
                    </row>
                </table>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="createPlaceEntries">
        <xsl:for-each select="//placeInfoList/placeInfoList">
            <xsl:sort lang="de" select="fiba:createTitle(.)"/>
            <xsl:variable name="entryTitle" select="fiba:createTitle(.)"/>
            <div>
                <xsl:attribute name="key" select="key"/>
                <xsl:attribute name="type">place</xsl:attribute>
                <head>
                    <xsl:value-of select="$entryTitle"/>
                </head>
                <table>
                    <row>
                        <cell>Ort</cell>
                        <cell>
                            <xsl:value-of select="locationName"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Region</cell>
                        <cell>
                            <xsl:value-of select="region"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Anmerkungen</cell>
                        <cell>
                            <xsl:value-of select="note"/>
                        </cell>
                    </row>
                    <row>
                        <cell>Koordinaten</cell>
                        <cell>
                            <xsl:if test="geoLocation">
                                <xsl:value-of select="geoLocation/longitude"/>
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="geoLocation/lattitude"/>
                            </xsl:if>
                        </cell>
                    </row>
                    <row>
                        <cell>Linked Data</cell>
                        <cell>
                            <xsl:choose>
                                <xsl:when test="count(controlledVocabularies/controlledVocabularies) &gt; 0">
                                    <list>
                                        <xsl:for-each select="controlledVocabularies/controlledVocabularies">
                                            <xsl:apply-templates select="."/>
                                        </xsl:for-each>
                                    </list>
                                </xsl:when>
                            </xsl:choose>
                        </cell>
                    </row>
                </table>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="createSaintEntries">
        <xsl:for-each select="//saintInfoList/saintInfoList">
            <xsl:sort lang="de" select="fiba:createTitle(.)"/>
            <xsl:variable name="entryTitle" select="fiba:createTitle(.)"/>
            <div>
                <xsl:attribute name="key" select="key"/>
                <xsl:attribute name="type">saint</xsl:attribute>
                <head>
                    <xsl:value-of select="$entryTitle"/>
                </head>
                <table>
                        <row>
                            <cell>Titel</cell>
                            <cell>
                                <xsl:value-of select="title"/>
                            </cell>
                        </row>
                    <row>
                        <cell>Heiligenlexikon</cell>
                        <cell>
                            <ref>
                                <xsl:attribute name="target" select="encyclopediaLink"/>
                                <xsl:value-of select="encyclopediaLink"/>
                            </ref>
                        </cell>
                    </row>
                    <row>
                        <cell>Linked Data</cell>
                        <cell>
                            <xsl:choose>
                                <xsl:when test="count(controlledVocabularies/controlledVocabularies) &gt; 0">
                                    <list>
                                        <xsl:for-each select="controlledVocabularies/controlledVocabularies">
                                            <xsl:apply-templates select="."/>
                                        </xsl:for-each>
                                    </list>
                                </xsl:when>
                            </xsl:choose>
                        </cell>
                    </row>
                </table>
            </div>
        </xsl:for-each>
    </xsl:template>
    
    
</xsl:stylesheet>