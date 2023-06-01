<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei" version="2.0">

    <xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8" doctype-public="html"/>

    <xsl:include href="../metadata/metadata.xsl"/>

    <xsl:variable name="printStyling">
        <xsl:text>
            @page {
                size: A4 portrait;
                margin: 2.5cm 2cm 2cm 2cm;
                @top-left {
                    font-family: 'Roboto', sans-serif;
                    content: 'Jägerstätter Edition';
                    font-size: 10pt;
                }
                @top-right {
                    font-family: 'Roboto', sans-serif;
                    content: counter(page) ' / ' counter(pages);
                    font-size: 10pt;
                }
                @footnote {
                    width: 100%;
                    border-top: 0.5pt solid black;
                    padding-top: 1rem;
                    orphans: 0;
                    widows: 0;
                }
            }
            body {
                font-family: 'Roboto', sans-serif;
            }
            h1, h2, h3, h4, h5, h6 {
                font-family: 'Roboto Slab', serif;
                font-weight: 400;
            }
            .footnote {
                float: footnote;
                margin-top: 10px;
                font-size: 10pt;
            }
            .paragraph {
                text-align: justify;
            }
            .addrLine, .address {
                font-family: 'Roboto Slab', serif;
            }
            .bibl, .hi, .quote {
                font-style: italic;
            }
            .pb {
                font-size: 70%;
                vertical-align: top;
            }
            ::footnote-call {
                counter-increment: footnote 1;
                content: "[" counter(footnote) "]";
                text-decoration: none;
                font-size: 80%;
                vertical-align: super;
                line-height: 0;
            }
            ::footnote-marker {
                content: counter(footnote) ". ";
            }
        </xsl:text>

    </xsl:variable>
    
    <xsl:variable name="docTitleString">
        <xsl:call-template name="createTitle"/>
    </xsl:variable>
    
    <xsl:variable name="docTitle" select="normalize-unicode($docTitleString)"/>

    <xsl:template match="/" priority="1">
        <html>
            <head>
                <title>
                    <xsl:value-of select="$docTitle"/>
                </title>
                <style>
                    <xsl:value-of select="$printStyling"/>
                </style>
            </head>
            <body>
                <div>
                    <h1>
                        <xsl:value-of select="$docTitle"/>
                    </h1>
                    <h2>Lesefassung</h2>
                    <div>
                        <xsl:apply-templates select="//tei:div[@type = 'Lesefassung']"/>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="tei:div">
        <div>
            <xsl:attribute name="class">
                <xsl:if test="@type">
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="@type"/>
                </xsl:if>
                <xsl:if test="@rend">
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="@rend"/>
                </xsl:if>
            </xsl:attribute>
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:p">
        <div class="paragraph">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:add">
        <span class="ffji-add">
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:addrLine">
        <div class="addrLine">
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:address">
        <div class="address">
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:bibl">
        <span class="bibl">
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:choice">
        <xsl:apply-templates select="tei:expan | tei:corr"/>
    </xsl:template>


    <xsl:template match="tei:damage">
        <span class="damage">[...]</span>
        <div class="footnote">Schaden am Original.</div>
    </xsl:template>

    <xsl:template match="tei:fw">
        <div class="ffji-fw">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:gap">
        <span class="ffji-gap">
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <xsl:template match="tei:head">
        <xsl:variable name="level" select="count(ancestor::tei:div[@type!='Lesefassung'])"/>
        <xsl:choose>
            <xsl:when test="$level=1">
                <h3>
                    <xsl:apply-templates/>
                </h3>
            </xsl:when>
            <xsl:when test="$level=2">
                <h4>
                    <xsl:apply-templates/>
                </h4>
            </xsl:when>
            <xsl:when test="$level=3">
                <h5>
                    <xsl:apply-templates/>
                </h5>
            </xsl:when>
            <xsl:otherwise>
                <h6>
                    <xsl:apply-templates/>
                </h6>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="tei:hi">
        <span class="hi">
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <xsl:template match="tei:item[parent::tei:list]">
        <li class="ffji-item">
            <xsl:apply-templates/>
        </li>
    </xsl:template>

    <xsl:template match="tei:lb">
        <br/>
    </xsl:template>

    <xsl:template match="tei:list">
        <ul class="ffji-hi">
            <xsl:apply-templates/>
        </ul>
    </xsl:template>

    <xsl:template match="tei:metamark">
        <div class="ffji-metamark">
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:note[ancestor::tei:note]" priority="1">
        <!-- Do nothing -->
    </xsl:template>

    <xsl:template match="tei:note[not(@type) and not(@hand)]">
        <div class="note">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:note[@type and not(@hand)]">
        <div class="footnote">
            <xsl:apply-templates />
        </div>
    </xsl:template>

    <xsl:template match="tei:note[@hand]">
        <div class="ffji-note">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="tei:pb">
    </xsl:template>

    <xsl:template match="tei:quote">
        <span class="quote">
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <xsl:template match="tei:ref[@target]">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template
        match="tei:ref[@type = 'comment' and (following::tei:* | text())[1][name() = 'note']]">
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="tei:surplus">
    </xsl:template>

    <xsl:template match="tei:unclear">
        <span class="ffji-unclear">
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <!-- For Normalized -->

    <xsl:template match="tei:salute">
        <br/>
        <xsl:apply-templates/>
    </xsl:template>


</xsl:stylesheet>