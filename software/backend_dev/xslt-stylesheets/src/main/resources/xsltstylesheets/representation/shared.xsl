<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:include href="../ffji-helper.xsl"/>
    
    <xsl:param name="markRsType"/>
    <xsl:param name="markRsKey"/>
    
    <xsl:template match="@*">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template name="createHeader">
        <div class="teiHeader">
            <xsl:apply-templates select="//tei:teiHeader" mode="createHeader"/>
        </div>
    </xsl:template>
    <xsl:template name="getMonth">
        <xsl:param name="month"/>
        <xsl:choose>
            <xsl:when test="$month='1' or $month='01'">
                <xsl:text>Januar</xsl:text>
            </xsl:when>
            <xsl:when test="$month='2' or $month='02'">
                <xsl:text>Februar</xsl:text>
            </xsl:when>
            <xsl:when test="$month='3' or $month='03'">
                <xsl:text>März</xsl:text>
            </xsl:when>
            <xsl:when test="$month='4' or $month='04'">
                <xsl:text>April</xsl:text>
            </xsl:when>
            <xsl:when test="$month='5' or $month='05'">
                <xsl:text>Mai</xsl:text>
            </xsl:when>
            <xsl:when test="$month='6' or $month='06'">
                <xsl:text>Juni</xsl:text>
            </xsl:when>
            <xsl:when test="$month='7' or $month='07'">
                <xsl:text>Juli</xsl:text>
            </xsl:when>
            <xsl:when test="$month='8' or $month='08'">
                <xsl:text>August</xsl:text>
            </xsl:when>
            <xsl:when test="$month='9' or $month='09'">
                <xsl:text>September</xsl:text>
            </xsl:when>
            <xsl:when test="$month='10'">
                <xsl:text>Oktober</xsl:text>
            </xsl:when>
            <xsl:when test="$month='11'">
                <xsl:text>November</xsl:text>
            </xsl:when>
            <xsl:when test="$month='12'">
                <xsl:text>Dezember</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$month"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="createReadableDate">
        <xsl:param name="date"/>
        <xsl:analyze-string select="$date" regex="(\d+)-(\d+)-(\d+)">
            <xsl:matching-substring>
                <xsl:value-of select="regex-group(3)"/>
                <xsl:text>.</xsl:text>
                <xsl:call-template name="getMonth">
                    <xsl:with-param name="month" select="regex-group(2)"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:value-of select="regex-group(1)"/>
            </xsl:matching-substring>
            <xsl:non-matching-substring>
                <xsl:value-of select="$date"/>
            </xsl:non-matching-substring>
        </xsl:analyze-string>
    </xsl:template>
    
    <xsl:template match="tei:teiHeader" mode="createHeader">
        <xsl:choose>
            <xsl:when test=".//tei:correspDesc">
                <xsl:variable name="sender" select=".//tei:correspAction[@type='sent']//tei:rs[@type='person']"/>
                <xsl:variable name="recipient" select=".//tei:correspAction[@type='received']//tei:rs[@type='person']"/>
                <xsl:variable name="place" select=".//tei:correspAction[@type='sent']//tei:rs[@type='place']"/>
                <xsl:variable name="dating" select=".//tei:correspAction[@type='sent']//tei:date/@when"/>
                
                <xsl:variable name="date">
                    <xsl:choose>
                        <xsl:when test="count($dating) = 0">
                            <xsl:text>unbekannt</xsl:text>
                        </xsl:when>
                        <xsl:when test="count($dating) = 1">
                            <xsl:call-template name="createReadableDate">
                                <xsl:with-param name="date" select="$dating"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="distinct-values($dating)">
                                <xsl:call-template name="createReadableDate">
                                    <xsl:with-param name="date" select="."/>
                                </xsl:call-template>
                                <xsl:if test="position() != last()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                    
                </xsl:variable>
                <h1>
                    <xsl:value-of select="$sender"/>
                    <xsl:text> an </xsl:text>
                    <xsl:value-of select="$recipient"/>
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="$place"/>
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="$date"/>
                </h1>
            </xsl:when>
            <xsl:otherwise>
                <h1>
                    <xsl:apply-templates select=".//tei:titleStmt/tei:title"/>
                </h1>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:div">
        <ffji-anchor>
            <xsl:attribute name="id">
                <xsl:call-template name="createDivId">
                    <xsl:with-param name="div" select="."/>
                </xsl:call-template>
            </xsl:attribute>
            <xsl:if test="@corresp">
                <xsl:attribute name="corresp" select="@corresp"/>
            </xsl:if>
        </ffji-anchor>
        <ffji-div>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
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
        </ffji-div>
    </xsl:template>
    
    <xsl:template match="tei:p">
        <xsl:if test="@xml:id or @corresp">
            <ffji-anchor>
                <xsl:attribute name="id">
                    <xsl:call-template name="createDivId">
                        <xsl:with-param name="div" select="."/>
                    </xsl:call-template>
                </xsl:attribute>
                <xsl:if test="@corresp">
                    <xsl:attribute name="corresp" select="@corresp"/>
                </xsl:if>
            </ffji-anchor>
        </xsl:if>
        <ffji-p>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div>
                <xsl:attribute name="class">
                    <xsl:text>ffji-para </xsl:text>
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
        </ffji-p>
    </xsl:template>
    
    <xsl:template match="tei:ab">
        <ffji-ab>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-ab>
    </xsl:template>
    
    <xsl:template match="tei:add">
        <ffji-add>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-add">
                <xsl:apply-templates/>
            </span>
        </ffji-add>
    </xsl:template>
    
    <xsl:template match="tei:cb">
        <ffji-cb>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-cb>
    </xsl:template>
    
    <xsl:template match="tei:damage">
        <ffji-damage>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-damage">
                <xsl:apply-templates/>
            </span>
        </ffji-damage>
    </xsl:template>
    
    <xsl:template match="tei:dateline">
        <ffji-dateline>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-dateline">
                <xsl:apply-templates/>
            </span>
        </ffji-dateline>
    </xsl:template>
    
    <xsl:template match="tei:del">
        <ffji-del>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-del">
                <xsl:apply-templates/>
            </span>
        </ffji-del>
    </xsl:template>
    
    
    <xsl:template match="tei:figure">
        <ffji-figure>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-figure">
                <xsl:apply-templates/>
            </span>
        </ffji-figure>
    </xsl:template>

    <xsl:template match="tei:fw">
        <ffji-fw>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-fw">
                <xsl:apply-templates/>
            </div>
        </ffji-fw>
    </xsl:template>
    
    <xsl:template match="tei:gap">
        <ffji-gap>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="gaptype">
                <xsl:choose>
                    <xsl:when test="parent::tei:subst">subst</xsl:when>
                    <xsl:when test="parent::tei:del">del</xsl:when>
                    <xsl:otherwise>gap</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-gap">
                <xsl:apply-templates/>
            </span>
        </ffji-gap>
    </xsl:template>

    <xsl:template match="tei:graphic">
        <ffji-graphic>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-graphic">
                <xsl:apply-templates/>
            </span>
        </ffji-graphic>
    </xsl:template>
    
    <xsl:template match="tei:head">
        <ffji-head>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="level" select="count(ancestor::tei:div)"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-head">
                <xsl:apply-templates/>
            </div>
        </ffji-head>
    </xsl:template>

    <xsl:template match="tei:hi">
        <ffji-hi>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-hi">
                <xsl:apply-templates/>
            </span>
        </ffji-hi>
    </xsl:template>
    
    <xsl:template match="tei:item">
        <ffji-item>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-item">
                <xsl:apply-templates/>
            </div>
        </ffji-item>
    </xsl:template>

    <xsl:template match="tei:lb">
        <ffji-lb>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
        </ffji-lb>
    </xsl:template>
    
    <xsl:template match="tei:list">
        <ffji-list>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <ul class="ffji-hi">
                <xsl:apply-templates/>
            </ul>
        </ffji-list>
    </xsl:template>
    
    <xsl:template match="tei:metamark">
        <ffji-metamark>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-metamark">
                <xsl:apply-templates/>
            </div>
        </ffji-metamark>
    </xsl:template>
    
    <xsl:template match="tei:note[not(@type) and not(@hand)]">
        <div class="note">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template name="shouldMark">
        <xsl:param name="node"/>
        <xsl:variable name="testApply">
            <xsl:apply-templates select="$node"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$testApply//@highlight[.='true']">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:note[@type and not(@hand)]">
        <xsl:choose>
            <xsl:when test="count(tei:ref)=1 and count(tei:*)=1 and count(text()[normalize-space()!='']=0)">
                <ffji-note-ref>
                    <xsl:attribute name="mode" select="$displayMode"/>
                    <xsl:attribute name="n" select="count(preceding::tei:note[not(@hand) and ancestor::tei:body]) + 1"/>
                    <xsl:attribute name="noteid">
                        <xsl:call-template name="countPrecedingAndParentNote">
                            <xsl:with-param name="noteElement" select="."/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <xsl:apply-templates select="@*[name()!='n' and name() != 'target']"/>
                    <xsl:for-each select="tei:ref">
                        <xsl:apply-templates select="@target"/>
                        <xsl:attribute name="directlink">
                            <xsl:choose>
                                <xsl:when test="contains(@target, ';')">false</xsl:when>
                                <xsl:when test="not(ancestor::tei:note)">false</xsl:when>
                                <xsl:otherwise>true</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="targettype">
                            <xsl:choose>
                                <xsl:when test="matches(@target, 'https?://')">external_link</xsl:when>
                                <xsl:when test="contains(@target, '.xml')">internal_link</xsl:when>
                                <xsl:when test="contains(@target, '.jpg')">internal_image</xsl:when>
                                <xsl:otherwise>unknown</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="mode" select="$displayMode"/>
                    </xsl:for-each>
                </ffji-note-ref>
            </xsl:when>
            <xsl:otherwise>
                <ffji-note>
                    <xsl:variable name="shouldMark">
                        <xsl:call-template name="shouldMark">
                            <xsl:with-param name="node" select=".//tei:*"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:variable name="isReferenced" select="$markRsType='BIBLE' and contains(., $markRsKey)"/>
                    <xsl:apply-templates select="@*"/>
                    <xsl:if test="$shouldMark='true' or $isReferenced">
                        <xsl:attribute name="highlight">true</xsl:attribute>
                    </xsl:if>
                    <xsl:attribute name="mode" select="$displayMode"/>
                    <xsl:attribute name="n" select="count(preceding::tei:note[not(@hand) and ancestor::tei:body]) + 1"/>
                    <xsl:attribute name="noteid">
                        <xsl:call-template name="countPrecedingAndParentNote">
                            <xsl:with-param name="noteElement" select="."/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <xsl:text>&#x200b;</xsl:text>
                </ffji-note>                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="tei:note[@hand]">
        <ffji-notehand>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-note">
                <xsl:apply-templates/>
            </div>
        </ffji-notehand>
    </xsl:template>
    
    <xsl:template match="tei:pb">
        <ffji-pb>
            <xsl:apply-templates select="@*[not(name()='facs')]"/>
            <xsl:attribute name="facs">
                <xsl:choose>
                    <xsl:when test="ends-with(lower-case(@facs), '.jpg')">
                        <xsl:value-of select="substring(@facs, 1, string-length(@facs) - 4)"/>
                    </xsl:when>
                    <xsl:when test="ends-with(lower-case(@facs), '.jpeg')">
                        <xsl:value-of select="substring(@facs, 1, string-length(@facs) - 5)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@facs"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="mode" select="$displayMode"/>
        </ffji-pb>
    </xsl:template>
    
    <xsl:template match="tei:quote">
        <ffji-quote>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-quote">
                <xsl:apply-templates/>
            </span>
        </ffji-quote>
    </xsl:template>

    <xsl:template match="tei:ref[@target]">
        <ffji-ref>
            <xsl:apply-templates select="@target"/>
            <xsl:attribute name="directlink">
                <xsl:choose>
                    <xsl:when test="contains(@target, ';')">false</xsl:when>
                    <xsl:when test="not(ancestor::tei:note)">false</xsl:when>
                    <xsl:otherwise>true</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="targettype">
                <xsl:choose>
                    <xsl:when test="matches(@target, 'https?://')">external_link</xsl:when>
                    <xsl:when test="contains(@target, '.xml')">internal_link</xsl:when>
                    <xsl:when test="contains(@target, '.jpg')">internal_image</xsl:when>
                    <xsl:otherwise>unknown</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-ref>
    </xsl:template>

    <xsl:template match="tei:ref[@type='comment' and (following::tei:*|text())[1][name()='note']]">
        <ffji-ref>
            <xsl:apply-templates select="@*"></xsl:apply-templates>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-ref">
                <xsl:apply-templates/>
            </span>
        </ffji-ref>
    </xsl:template>

    <xsl:template match="tei:rs[@type='person']">
        <ffji-persname>
            <xsl:variable name="targets">
                <xsl:choose>
                    <xsl:when test="@ref and @key">
                        <xsl:value-of select="replace(@ref, '\s+',';')"/>
                        <xsl:text>;</xsl:text>
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                    <xsl:when test="@ref">
                        <xsl:value-of select="replace(@ref, '\s+', ';')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@key"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:attribute name="targets" select="$targets"/>
            <xsl:attribute name="cert" select="@cert"/>
            <xsl:attribute name="name">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="key">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="ref">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="highlight">
                <xsl:choose>
                    <xsl:when test="$markRsKey=@key or @ref=concat('#', $markRsKey) and ($markRsType='PERSON' or $markRsType='SAINT')">true</xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates/>
        </ffji-persname>
    </xsl:template>

    <xsl:template match="tei:rs[@type='place']">
        <ffji-placename>
            <xsl:attribute name="name">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="key">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="highlight">
                <xsl:choose>
                    <xsl:when test="$markRsKey=@key and $markRsType='PLACE'">true</xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates/>
        </ffji-placename>
    </xsl:template>

    <xsl:template match="tei:rs[@type='org']">
        <ffji-orgname>
            <xsl:attribute name="name">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="key">
                <xsl:choose>
                    <xsl:when test="@ref">
                        <xsl:value-of select="@ref"/>
                    </xsl:when>
                    <xsl:when test="@key">
                        <xsl:value-of select="@key"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="highlight">
                <xsl:choose>
                    <xsl:when test="$markRsKey=@key and $markRsType='CORPORATION'">true</xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates/>
        </ffji-orgname>
    </xsl:template>
    

    <xsl:template match="tei:stamp">
        <ffji-stamp>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <div class="ffji-stamp">
                <xsl:apply-templates/>
            </div>
        </ffji-stamp>
    </xsl:template>
    
    <xsl:template match="tei:subst">
        <ffji-subst>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-subst">
                <xsl:apply-templates select="tei:*"/>
            </span>
        </ffji-subst>
    </xsl:template>

    <xsl:template match="tei:supplied">
        <ffji-supplied>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-supplied">
                <xsl:apply-templates />
            </span>
        </ffji-supplied>
    </xsl:template>
    
    <xsl:template match="tei:surplus">
        <ffji-surplus>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-surplus">
                <xsl:apply-templates />
            </span>
        </ffji-surplus>
    </xsl:template>

    <xsl:template match="tei:unclear">
        <ffji-unclear>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <span class="ffji-unclear">
                <xsl:apply-templates />
            </span>
        </ffji-unclear>
    </xsl:template>
    
    <!-- For Normalized -->
    
    <xsl:template match="tei:abbr">
        <ffji-abbr>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="parent::tei:choice">
                <xsl:attribute name="inchoice">true</xsl:attribute>
            </xsl:if>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-abbr>
    </xsl:template>
    
    <xsl:template match="tei:corr">
        <ffji-corr>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="inchoice">
                <xsl:choose>
                    <xsl:when test="parent::tei:choice">true</xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-corr>
    </xsl:template>
    
            
    <xsl:template match="tei:choice">
        <ffji-choice>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates select="tei:*"/>
        </ffji-choice>
    </xsl:template>

    <xsl:template match="tei:expan">
        <ffji-expan>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-expan>
    </xsl:template>
    
    <xsl:template match="tei:sic">
        <ffji-sic>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="parent::tei:choice">
                <xsl:attribute name="inchoice">true</xsl:attribute>
            </xsl:if>
            <xsl:attribute name="mode" select="$displayMode"/>
            <xsl:apply-templates/>
        </ffji-sic>
    </xsl:template>
    
    
    <xsl:template match="tei:salute">
        <br/>
        <xsl:apply-templates/>
    </xsl:template>        
    
    
    <xsl:template name="collectHands">
        <ffji-handsinfo>
            <xsl:variable name="hands" select="distinct-values(//tei:body//(tei:div|tei:add|tei:note)/@hand)" />
            <xsl:attribute name="hands" select="string-join($hands, ';')"></xsl:attribute>
        </ffji-handsinfo>
    </xsl:template>
            
    <xsl:template name="countPrecedingAndParentNote">
        <xsl:param name="noteElement"/>
        <xsl:value-of select="count($noteElement/preceding::tei:note) + count($noteElement/ancestor::tei:note) + 1"/>
    </xsl:template>
    
</xsl:stylesheet>
