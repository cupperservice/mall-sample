package cupper.hj2.mall.models.values

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

data class Password(val password: String) {
    fun hashedValue(): String {
        val hash = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return DatatypeConverter.printHexBinary(hash)
    }
}
