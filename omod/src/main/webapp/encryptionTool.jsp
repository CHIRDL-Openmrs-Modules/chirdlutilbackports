<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require allPrivileges="Manage CHIRDLUTILBACKPORTS" otherwise="/login.htm" redirect="/module/chirdlutilbackports/encryptionTool.form" />
<link href="${pageContext.request.contextPath}/moduleResources/chirdlutilbackports/encryptionTool.css" type="text/css" rel="stylesheet" />

<form name="input" action="encryptionTool.form" method="post">

<p>Enter string to encrypt </p>
<input type="text" name="encrypt_text" value="${encrypt_text}"/>
<input type="submit" name = "encrypt_action" value="Encrypt"/>
<p>${encryptionKeyString}</p>
<p>Encrypted value:  <strong>${encryptedValue}</strong></p>
<br>
<p>Enter string to decrypt </p>
<input type="text" name="decrypt_text" value="${decrypt_text}"/>
<input type="submit" name = "decrypt_action" value="Decrypt"/>
<p>Decrypted value:  <strong>${decryptedValue}</strong></p>
</form>
<p>
<c:if test="${not empty error}">
   Error: ${error}
</c:if>
</p>

<%@ include file="/WEB-INF/template/footer.jsp" %>