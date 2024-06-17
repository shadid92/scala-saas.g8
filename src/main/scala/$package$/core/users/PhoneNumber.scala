package $package$.core.users

import com.google.i18n.phonenumbers.PhoneNumberUtil
import $package$.prelude.{*, given}

class PhoneNumber private (val value: String) {
  override def toString = value
  lazy val countryCode: Int = PhoneNumber.pnUtil
    .parse(s"+\${value.filterNot(_ == '+')}", "")
    .getCountryCode()
}
object PhoneNumber {

  private lazy val pnUtil = PhoneNumberUtil.getInstance()

  def isValid(s: String): Option[PhoneNumber] = {
    try {
      val pn = pnUtil.parse(s"+\$s", "")
      val isValid = pnUtil.isValidNumber(pn) && s.length > 9
      if isValid then
        PhoneNumber(
          pn.getCountryCode().toString + pn.getNationalNumber.toString
        ).some
      else None
    } catch {
      case _ => None
    }
  }
  def fromStringValidetionEither(s: String) =
    Either.fromOption(isValid(s), Exception(parseErrorMsg(s)))

  def fromString(s: String): Option[PhoneNumber] = isValid(s)

  def parseErrorMsg(phoneStr: String) = s"Invalid phone number: \$phoneStr"

  def fromStringEither(s: String) =
    Either.fromOption(fromString(s), Exception(parseErrorMsg(s)))

  def countryCode(phone: PhoneNumber) =
    pnUtil.parse(phone.value, "JO").getCountryCode

  given Meta[PhoneNumber] = Meta[String].imap(PhoneNumber(_))(_.value)

  given Encoder[PhoneNumber] = Encoder[String].contramap(_.value)

  given Decoder[PhoneNumber] =
    Decoder[String].emap(fromStringEither(_).leftMap(_.getMessage))

  given HttpSchema[PhoneNumber] = HttpSchema
    .string[PhoneNumber]
    .description(
      "Phone number, whether it begins with '+', '00', '0' (defaults to 966), or just the country code. Also valid if it contains spaces, parentheses, or '-'s."
    )
    .encodedExample("(962) 797 559 507")
}
