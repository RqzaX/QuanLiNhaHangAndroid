package com.example.nhahang
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update

@Dao
interface ChiTietHoaDonDAO {
    @Insert
    fun InsertChiTietHoaDon(chiTietHoaDon: ChiTietHoaDon)

    @Update
    fun UpdateChiTietHoaDon(chiTietHoaDon: ChiTietHoaDon)

    @Delete
    fun DeleteChiTietHoaDon(chiTietHoaDon: ChiTietHoaDon)

    @Query("SELECT * FROM ChiTietHoaDon")
    fun getAllChiTietHoaDon(): List<ChiTietHoaDon>

    @Query("""
    SELECT SUM(soLuong)
FROM ChiTietHoaDon
WHERE maHoaDon IN (
    SELECT maHoaDon
    FROM HoaDon
    WHERE thoiGianDat = :ngayHienTai And trangThai = "Đã thanh toán"
)
""")
    fun getTongSoLuongTrongNgay(ngayHienTai: String): Int
//
//    @Query("""
//    SELECT SUM(tongTienMon)
//FROM ChiTietHoaDon
//WHERE maHoaDon IN (
//    SELECT maHoaDon
//    FROM HoaDon
//    WHERE thoiGianDat = :ngayHienTai And trangThai = "Đã thanh toán"
//)
//""")
//    fun getTongSoTienTrongNgay(ngayHienTai: String): Int
//
//    @Query("""
//    SELECT SUM(tongTienMon)
//    FROM ChiTietHoaDon
//    WHERE maHoaDon IN (
//        SELECT maHoaDon
//        FROM HoaDon
//        WHERE strftime('%m/%Y', thoiGianDat) = :thangNam
//    )
//""")
//    fun getTongTienThang(thangNam : String): Int


    @Query("""
    SELECT ChiTietHoaDon.*
    FROM ChiTietHoaDon 
    INNER JOIN HoaDon ON ChiTietHoaDon.maHoaDon = HoaDon.maHoaDon 
    INNER JOIN MonAn ON MonAn.maMon = ChiTietHoaDon.maMon
    WHERE ChiTietHoaDon.maHoaDon = :maHoaDon AND ChiTietHoaDon.maMon = :maMon
""")
    fun getCTHDByMaHDMonAn(maHoaDon: Int, maMon: Int): ChiTietHoaDon
    @Query("""
    Select ChiTietHoaDon.maChiTietHoaDon AS idBillInfo, 
        ChiTietHoaDon.maHoaDon AS idBill, 
        MonAn.tenMon AS foodName, 
        MonAn.anh AS foodImg, 
        ChiTietHoaDon.soLuong AS quantity, 
        ChiTietHoaDon.tongTienMon AS price
    FROM ChiTietHoaDon
    INNER JOIN HoaDon ON ChiTietHoaDon.maHoaDon = HoaDon.maHoaDon
    INNER JOIN MonAn ON MonAn.maMon = ChiTietHoaDon.maMon
    WHERE ChiTietHoaDon.maHoaDon = :maHoaDon
""")
    fun getCTHD(maHoaDon: Int): List<BillInfo>
}