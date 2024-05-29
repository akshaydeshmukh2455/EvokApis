package com.npst.evok.api.evok_apis.controller;

public class CallbackDecryptor {
	public static void main(String[] args) {
        String encryptedString = "ET\\/IcX7BAAy51V+iVxH+craAjl7s7fZoEPpixIF\\/jZeKrmWx1JAumfTSKAk\\/YHqVjny6drH3BNSms9mxv7n4gg\\/qqS91UOSHM6W2ZEVk0N481PB7GjUN0YSCdjHcXg+TUm8hUZyAIOztLPrcdhjSqBNvFuVxQUvJtysexcJmDgs03XOYQfpLORJuEYcJQMdGFmnw4NoReMX\\/pG15DkFTdYUcQCng5aWj6RxEZ45fmk\\/cXFeq+mB6Qh3R2Y+48kJb7KJonRtJmQiRD63pBKm59l4Wpg0mwB\\/8coZpGKkg825I6GSV6J5TvlZUeMe1AauTLSEJjvsNRNDjqfYLRTt3pcDM2LA4bl7iQe2JX4Ecrx4DNmoH2WppwvCuegxh6lX1G7+hKBMnFUGm4cRIaKnj\\/t35t4sW39GbzcL81L2NXo+jrD6BJVzO8vAC2lThWQjvUnKqHVXDAQaNZVpCuBLiskGkE6CaWEupghzBmr6m6Lt\\/9GjKtdo56sUUfvRiQbXEpAxHp+BPfWzg9oq4yoApSowfGZ4RSb9MYAmfjk2aAL51XH9ZlVjUAiwplFPGpRe79uq1wKbzt50nfUBWheEitZTvFKnNs8qiSY0TdGsdvh4kaB+zeCkVLTB0JW9h3Ims\\/lXnNl1vZ2tbX2U5NMP\\/8Q==";

        // Remove occurrences of \/
        String processedString = encryptedString.replaceAll("\\\\/", "");

        System.out.println("Processed String: " + processedString);
    }
}
