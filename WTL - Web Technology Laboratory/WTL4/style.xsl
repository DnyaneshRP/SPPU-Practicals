<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <head>
    <style>
      table { width: auto; }
      th { background-color: #4CAF50; color: white; padding: 10px; }
      td { border: 1px solid #ddd; padding: 8px; text-align: center; }
      tr:nth-child(even) { background-color: #f2f2f2; }
    </style>
  </head>
  <body>
    <h2>Employee Information</h2>
    <table>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Designation</th>
        <th>Salary</th>
      </tr>
      <xsl:for-each select="company/employee">
      <tr>
        <td><xsl:value-of select="id"/></td>
        <td><xsl:value-of select="name"/></td>
        <td><xsl:value-of select="designation"/></td>
        <td><xsl:value-of select="salary"/></td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>