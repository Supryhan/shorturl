package shortner.utils

import munit.FunSuite

class EncodeDecodeUtilsTest extends FunSuite {

  test("encode should return correct encoded string") {
    assertEquals(EncodeDecodeUtils.encode(0), "0")
    assertEquals(EncodeDecodeUtils.encode(1), "1")
    assertEquals(EncodeDecodeUtils.encode(62), "10")
    assertEquals(EncodeDecodeUtils.encode(123), "1Z")
    assertEquals(EncodeDecodeUtils.encode(123456789), "8m0Kx")
  }

  test("decode should return correct number") {
    assertEquals(EncodeDecodeUtils.decode("0"), 0L)
    assertEquals(EncodeDecodeUtils.decode("1"), 1L)
    assertEquals(EncodeDecodeUtils.decode("10"), 62L)
    assertEquals(EncodeDecodeUtils.decode("1Z"), 123L)
    assertEquals(EncodeDecodeUtils.decode("8m0Kx"), 123456789L)
  }

  test("decode and encode should be inverses") {
    val numbers = List(0L, 1L, 10L, 62L, 123L, 123456L, 123456789L)
    numbers.foreach { number =>
      assertEquals(EncodeDecodeUtils.decode(EncodeDecodeUtils.encode(number)), number)
    }
  }
}
