/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.Security;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Tool accessible from the OpenMRS Administration page that encrypts or decrypts 
 * an entered string and displays the result.
 * @author Meena Sheley
 * 
 */

@Controller
@RequestMapping(value = "module/chirdlutilbackports/encryptionTool.form")
public class EncryptionToolController
{

   
	protected final Log log = LogFactory.getLog(getClass());
    private static final String FORM_VIEW = "/module/chirdlutilbackports/encryptionTool";
    
    /** Parameters */
    private static final String PARAMETER_ENCRYPTION_KEY_STRING = "encryptionKeyString";
    private static final String PARAMETER_ENCRYPT_TEXT = "encrypt_text";
    private static final String PARAMETER_DECRYPT_TEXT = "decrypt_text";   
    private static final String PARAMETER_ENCRYPTED_VALUE = "encryptedValue";
    private static final String PARAMETER_DECRYPTED_VALUE = "decryptedValue";

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap map) { 
        return FORM_VIEW;
    }
    
  
    /**Calculates the encrypted value of a string using the encryption key from the 
     * OpenMRS runtime properties file. 
     * @param textToEncrypt - string to be encrypted.
     * @param request
     * @param model
     * @return
     */
   @RequestMapping(method = RequestMethod.POST, params = "encrypt_action")
    public String processEncrypt(@RequestParam(PARAMETER_ENCRYPT_TEXT) String textToEncrypt,  
            HttpServletRequest request, ModelMap model) {
    
        String runtimePropertyKeyText = Context.getRuntimeProperties().getProperty(OpenmrsConstants.ENCRYPTION_KEY_RUNTIME_PROPERTY,
                OpenmrsConstants.ENCRYPTION_KEY_DEFAULT);
      
        model.addAttribute(PARAMETER_ENCRYPT_TEXT, textToEncrypt);
      
        try {
            if (StringUtils.isNotBlank(textToEncrypt)) {  
                    //Uses the openmrs runtime properties keys and vectors.
                    String encryptedValue = Security.encrypt(textToEncrypt);
                    model.addAttribute(PARAMETER_ENCRYPTED_VALUE, encryptedValue);
                    model.addAttribute(PARAMETER_ENCRYPTION_KEY_STRING, "Encrypted key used: " + runtimePropertyKeyText);   
            }
                                   
            
        }  catch (Exception e) {           
               log.error("Unable to encrypt this string. Verify that openmrs runtime properties file exists with encryption settings.");
        }
       
        return FORM_VIEW;
    }
   
   /** Decrypts the encrypted text and displays the decrypted result. 
 * @param textToDecrypt - string to be decrypted.
 * @param request
 * @param model
 * @return
 */
@RequestMapping(method = RequestMethod.POST, params = "decrypt_action")
   public String processDecrypt(@RequestParam(PARAMETER_DECRYPT_TEXT) String textToDecrypt,  
          HttpServletRequest request, ModelMap model) {
   
       model.addAttribute(PARAMETER_DECRYPT_TEXT, textToDecrypt);
       
       try {
           if (StringUtils.isNotBlank(textToDecrypt)) {  
           
               String decryptedValue = Security.decrypt(textToDecrypt);
               model.addAttribute(PARAMETER_DECRYPTED_VALUE, decryptedValue);
            }
       } catch (Exception e) {
            model.addAttribute("error", "Unable to decrypt entered value. ");
            log.error("Unable to decrypt entered text: " + textToDecrypt, e);
        } finally {
            model.addAttribute(PARAMETER_ENCRYPTION_KEY_STRING, ""); 
            model.addAttribute(PARAMETER_ENCRYPTED_VALUE, "");
        }
      
       return FORM_VIEW;
   }
	 

}
