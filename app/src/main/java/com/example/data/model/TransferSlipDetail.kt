package com.example.data.model

data class TransferSlipDetail(
    val slipNumber: Int,
    val refCode: String,
    val bankDisplayName: String,
    val senderName: String,
    val senderBank: String,
    val senderAccount: String,
    val receiverName: String,
    val receiverBank: String,
    val receiverAccount: String,
    val isPromptPay: Boolean,
    val amountFormatted: String,
    val feeFormatted: String = "0.00 บาท",
    val dateTimeFormatted: String,
    val drawableName: String = "slip_%02d".format(slipNumber)
)

object TransferSlipRepository {
    val slips: List<TransferSlipDetail> = listOf(
        TransferSlipDetail(
            slipNumber = 1,
            refCode = "016279134850BPP03079",
            bankDisplayName = "ธนาคารกสิกรไทย (K PLUS)",
            senderName = "น.ส. เกศินี แ",
            senderBank = "ธนาคารกสิกรไทย",
            senderAccount = "xxx-x-x5442-x",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "xxx-xxx-6001",
            isPromptPay = true,
            amountFormatted = "25.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "6 ต.ค. 69 เวลา 13:48 น."
        ),
        TransferSlipDetail(
            slipNumber = 2,
            refCode = "016286101921APP12368",
            bankDisplayName = "ธนาคารกสิกรไทย (K PLUS)",
            senderName = "น.ส. เกศินี แ",
            senderBank = "ธนาคารกสิกรไทย",
            senderAccount = "xxx-x-x5442-x",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "xxx-xxx-6001",
            isPromptPay = true,
            amountFormatted = "75.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 69 เวลา 10:19 น."
        ),
        TransferSlipDetail(
            slipNumber = 3,
            refCode = "Aa336ee3aeb314fb2",
            bankDisplayName = "ธนาคารกรุงไทย (Krungthai NEXT)",
            senderName = "น.ส. กอบคำ ใ***",
            senderBank = "ธนาคารกรุงไทย",
            senderAccount = "XXX-X-XX561-1",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "XXX-XXX-6001",
            isPromptPay = true,
            amountFormatted = "37.50 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 2569 เวลา 21:24 น."
        ),
        TransferSlipDetail(
            slipNumber = 4,
            refCode = "016279134747CPP04681",
            bankDisplayName = "ธนาคารกสิกรไทย (K PLUS)",
            senderName = "น.ส. กัลยารัตน์ ส",
            senderBank = "ธนาคารกสิกรไทย",
            senderAccount = "xxx-x-x1433-x",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "xxx-xxx-6001",
            isPromptPay = true,
            amountFormatted = "25.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "6 ต.ค. 69 เวลา 13:47 น."
        ),
        TransferSlipDetail(
            slipNumber = 5,
            refCode = "A2db58bf5f98d41d5",
            bankDisplayName = "ธนาคารกรุงไทย (Krungthai NEXT)",
            senderName = "น.ส. กอบคำ ใ***",
            senderBank = "ธนาคารกรุงไทย",
            senderAccount = "XXX-X-XX561-1",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "XXX-XXX-6001",
            isPromptPay = true,
            amountFormatted = "75.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 2569 เวลา 11:07 น."
        ),
        TransferSlipDetail(
            slipNumber = 6,
            refCode = "A2daf358788334e9d",
            bankDisplayName = "ธนาคารกรุงไทย (Krungthai NEXT)",
            senderName = "น.ส. กอบคำ ใ***",
            senderBank = "ธนาคารกรุงไทย",
            senderAccount = "XXX-X-XX561-1",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "XXX-XXX-6001",
            isPromptPay = true,
            amountFormatted = "25.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "06 ต.ค. 2569 เวลา 13:46 น."
        ),
        TransferSlipDetail(
            slipNumber = 7,
            refCode = "A2db58bf5f98d41d5",
            bankDisplayName = "ธนาคารกรุงไทย (Krungthai NEXT)",
            senderName = "น.ส. กอบคำ ใ***",
            senderBank = "ธนาคารกรุงไทย",
            senderAccount = "XXX-X-XX561-1",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "XXX-XXX-6001",
            isPromptPay = true,
            amountFormatted = "75.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 2569 เวลา 11:07 น."
        ),
        TransferSlipDetail(
            slipNumber = 8,
            refCode = "A50cab163fd3f4257",
            bankDisplayName = "ธนาคารกรุงไทย (Krungthai NEXT)",
            senderName = "น.ส. กอบคำ ใ***",
            senderBank = "ธนาคารกรุงไทย",
            senderAccount = "XXX-X-XX561-1",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "XXX-XXX-6001",
            isPromptPay = true,
            amountFormatted = "37.50 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 2569 เวลา 17:02 น."
        ),
        TransferSlipDetail(
            slipNumber = 9,
            refCode = "016287123628BPP03908",
            bankDisplayName = "ธนาคารกสิกรไทย (K PLUS)",
            senderName = "น.ส. เกศินี แ",
            senderBank = "ธนาคารกสิกรไทย",
            senderAccount = "xxx-x-x5442-x",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "xxx-xxx-6001",
            isPromptPay = true,
            amountFormatted = "37.50 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "14 ต.ค. 69 เวลา 12:36 น."
        ),
        TransferSlipDetail(
            slipNumber = 10,
            refCode = "016286101921APP12368",
            bankDisplayName = "ธนาคารกสิกรไทย (K PLUS)",
            senderName = "น.ส. เกศินี แ",
            senderBank = "ธนาคารกสิกรไทย",
            senderAccount = "xxx-x-x5442-x",
            receiverName = "น.ส. กานต์ธิดา อ้ายใจ",
            receiverBank = "พร้อมเพย์",
            receiverAccount = "xxx-xxx-6001",
            isPromptPay = true,
            amountFormatted = "75.00 บาท",
            feeFormatted = "0.00 บาท",
            dateTimeFormatted = "13 ต.ค. 69 เวลา 10:19 น."
        )
    )

    fun getSlipForInquiry(inquiryId: String): TransferSlipDetail {
        val number = inquiryId.filter { it.isDigit() }.takeLast(3).toIntOrNull() ?: 1
        // 1 to 10 maps directly to slips index 0..9
        // If inquiries exceed 10, loop back to file 1 (index 0)
        val index = (number - 1).coerceAtLeast(0) % slips.size
        return slips[index]
    }
}
