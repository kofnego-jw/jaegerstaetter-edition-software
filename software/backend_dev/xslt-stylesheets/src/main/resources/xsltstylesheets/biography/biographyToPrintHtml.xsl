<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei" version="2.0">

    <xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8" />
    
    <xsl:param name="port">60725</xsl:param>

    <!-- <xsl:include href="../metadata/metadata.xsl"/> -->

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
            * {
                line-height: 150%;
            }
            body {
                font-family: 'Roboto', sans-serif;
            }
            h1, h2, h3, h4, h5, h6 {
                font-family: 'Roboto Slab', serif;
                font-weight: 400;
            }
            div {
                text-align: justify;
            }
            div.paragraph {
                margin: 1rem 0;
            }
            div.pseudohead {
                margin: 2rem 0 1rem;
            }
            .footnote {
                float: footnote;
                margin-top: 10px;
                font-size: 10pt;
                text-align: left;
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
            .biography {
            
            }
            .biographyFootnote {
                font-size: 70%;
                vertical-align: top;
                top: -7px;
                position: relative;
            }
            .figure {
                text-align: center;
                font-size: small;
                justify-content: center;
                width: 100%;
                margin-bottom: 1rem;
            }
            .figure img.img-fluid {
                margin-bottom: 0.5rem;
                max-width: 33%;
                max-height: 33%;
            }
            .figure .figDesc {
                width: 100%;
                text-align: center;
            }
            .clear {
                clear: both;
            }
            
            .italic {
                font-variant: italic;
            }
            .superscript {
                vertical-align: super;
                font-size: 60%;
            }
            .subscript {
                vertical-align: sub;
                font-size: 70%;
            }
            .bold {
                font-weight: bold;
            }
            .textAndImage {
            }
            .text {
            }
            .images {
            }
            ::footnote-call {
                counter-increment: footnote 1;
                content: counter(footnote);
                font-size: 70%;
                vertical-align: top;
                top: -7px;
                position: relative;
            }
            ::footnote-marker {
                content: counter(footnote) ". ";
            }
            
        </xsl:text>

    </xsl:variable>
    
    <xsl:variable name="author" select="//tei:titleStmt/tei:author"/>
    
    <xsl:variable name="docTitle" select="//tei:titleStmt/tei:title"/>
    
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
                <div class="wholePage">
                    <h1>
                        <xsl:value-of select="$docTitle"/>
                    </h1>
                    <div class="author">
                        <p>Verfasst von: <xsl:value-of select="$author"/></p>
                    </div>
                    <div class="biography">
                        <xsl:apply-templates select="//tei:body"></xsl:apply-templates>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tei:body/tei:head">
        <!-- Ignore this -->
    </xsl:template>
    
    <xsl:template match="tei:head">
        <xsl:variable name="level" select="count(ancestor::tei:div)"/>
        <xsl:choose>
            <xsl:when test="$level=1">
                <h2>
                    <xsl:apply-templates/>
                </h2>
            </xsl:when>
            <xsl:when test="$level=2">
                <h3>
                    <xsl:apply-templates/>
                </h3>
            </xsl:when>
            <xsl:when test="$level=3">
                <h4>
                    <xsl:apply-templates/>
                </h4>
            </xsl:when>
            <xsl:otherwise>
                <h5>
                    <xsl:apply-templates/>
                </h5>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="tei:div">
        <div class="textAndImage">
            <div class="text">
                <xsl:apply-templates />
            </div>
            <div class="images">
                <xsl:apply-templates mode="image"></xsl:apply-templates>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:div" mode="footnote">
        <xsl:apply-templates mode="footnote"/>
    </xsl:template>

    <xsl:template match="tei:p">
        <xsl:choose>
            <xsl:when test="string(.) = string(./tei:hi[1][contains(@rend,'bold')])">
                <div>
                    <xsl:attribute name="class">
                        <xsl:text>pseudohead</xsl:text>
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
            </xsl:when>
            <xsl:otherwise>
                <div>
                    <xsl:attribute name="class">
                        <xsl:text>paragraph</xsl:text>
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
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:p" mode="footnote">
        <xsl:if test="count(following-sibling::tei:*) &gt; 0">
            <br/>
        </xsl:if>
        <xsl:apply-templates mode="footnote"/>
    </xsl:template>
    
    <xsl:template match="tei:list">
        <xsl:choose>
            <xsl:when test="@rend='bulleted'">
                <ul type="disc">
                    <xsl:apply-templates/>
                </ul>
            </xsl:when>
            <xsl:otherwise>
                <ol>
                    <xsl:apply-templates/>
                </ol>
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
            <xsl:attribute name="class">
                <xsl:text>hi </xsl:text>
                <xsl:if test="@rend">
                    <xsl:value-of select="@rend"/>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:if test="@style">
                    <xsl:value-of select="@style"/>
                </xsl:if>
            </xsl:attribute>
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:hi" mode="footnote">
        <span>
            <xsl:attribute name="class">
                <xsl:text>hi </xsl:text>
                <xsl:if test="@rend">
                    <xsl:value-of select="@rend"/>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:if test="@style">
                    <xsl:value-of select="@style"/>
                </xsl:if>
            </xsl:attribute>
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <xsl:template match="tei:note" mode="#default">
        <span class="footnote">
            <xsl:apply-templates mode="footnote"/>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:note" mode="createFootNote">
        <xsl:variable name="number" select="count(preceding::tei:note[ancestor::tei:body]) + 1"/>
        <tr>
            <td>
                <xsl:value-of select="$number"/>
            </td>
            <td>
                <xsl:apply-templates/>
            </td>
        </tr>
    </xsl:template>
    
    <xsl:template match="tei:figure">
        <!-- Do nothing -->
    </xsl:template>
    
    <xsl:template match="tei:*" mode="image">
        <xsl:apply-templates mode="image"/>
    </xsl:template>
    <xsl:template match="text()" mode="image">
        <!-- Do nothing -->
    </xsl:template>
    
    <xsl:template match="tei:figure" mode="image">
        <div class="figure">
            <xsl:if test="tei:graphic">
                <img class="img img-fluid">
                    <xsl:attribute name="alt" select="tei:figDesc"/>
                    <xsl:attribute name="src">
                        <xsl:text>http://localhost:</xsl:text>
                        <xsl:value-of select="$port"/>
                        <xsl:text>/api/biography/images/</xsl:text>
                        <xsl:value-of select="tei:graphic/@url"/>
                    </xsl:attribute>
                </img>
            </xsl:if>
            <xsl:if test="tei:figDesc">
                <div class="figDesc">
                    <xsl:apply-templates select="tei:figDesc"/>
                </div>
            </xsl:if>
        </div>
        <div class="clear"></div>
    </xsl:template>


</xsl:stylesheet>
