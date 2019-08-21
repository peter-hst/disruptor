import CryptoJS from 'crypto-js'
const key = CryptoJS.enc.Utf8.parse("1234123412ABCDEF")
const iv = CryptoJS.enc.Utf8.parse('ABCDEF1234123412')

export default {
 /**
 * AES加密 ：字符串 key iv  返回base64 
 */
    encrypt(word, keyStr = key, ivStr = iv) {      
        let srcs = CryptoJS.enc.Utf8.parse(word);
        var encrypted = CryptoJS.AES.encrypt(srcs, keyStr, {
          iv: ivStr,
          mode: CryptoJS.mode.CBC,
          padding: CryptoJS.pad.ZeroPadding
        });
       // console.log("-=-=-=-", encrypted.ciphertext)
        return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
      
      },
 /**
   * AES 解密 ：字符串 key iv  返回base64 
   *
   */
  decrypt(word, keyStr = key, ivStr = iv) {

    let base64 = CryptoJS.enc.Base64.parse(word);
    let src = CryptoJS.enc.Base64.stringify(base64);
  
    var decrypt = CryptoJS.AES.decrypt(src, keyStr, {
      iv: ivStr,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.ZeroPadding
    });
  
    var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
  }
}
 