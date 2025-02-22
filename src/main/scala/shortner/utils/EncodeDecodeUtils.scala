package shortner.utils

object EncodeDecodeUtils:

  private val alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val base = alphabet.length

  def encode(number: Long): String = {
    var n = number
    var encoded = ""
    while (n > 0) {
      encoded = alphabet.charAt((n % base).toInt) + encoded
      n /= base
    }
    if (encoded.isEmpty) alphabet.head.toString else encoded
  }

  def decode(url: String): Long =
    url.foldLeft(0L) { (acc, char) =>
      acc * base + alphabet.indexOf(char)
    }
end EncodeDecodeUtils