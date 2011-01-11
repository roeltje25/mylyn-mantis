<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">


		<xsl:for-each select="document/body/release">
Version <xsl:value-of select="@version"/> released

A minor update has been uploaded at http://mylyn-mantis.sourceforge.net/eclipse/update/.
[list]
<xsl:for-each select="action">[*] <xsl:value-of select="@issue"/>: <xsl:value-of select="."/> ( <xsl:value-of select="@dev"/> )[/*]
</xsl:for-each>[/list]
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>