package com.example.nhahang
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update

@Dao
interface HoaDonDAO {
    @Insert
    fun InsertHoaDon(hoaDon: HoaDon)

    @Update
    fun updateHoaDon(hoaDon: HoaDon)

    @Delete
    fun deleteHoaDon(hoaDon: HoaDon)

    @Query("SELECT count(*) FROM HoaDon where trangThai = 'Đã thanh toán' and thoiGianDat = :ngayHomNay")
    fun getAllHoaDonTrongNgay(ngayHomNay : String): Int
    @Query("SELECT * FROM HoaDon where MaBan = :maBan and trangThai = 'chưa thanh toán'")
    fun getDonHangByTenBan(maBan : Int): HoaDon
    @Query("SELECT * FROM HoaDon where trangThai = 'chưa thanh toán' Order By HoaDon.maHoaDon DESC limit 1")
    fun getNewHoaDon(): HoaDon
    @Query("SELECT sum(tongTien) FROM HoaDon where trangThai = 'Đã thanh toán' and thoiGianDat = :ngayHomNay ")
    fun getDoanhThuTrongNgay(ngayHomNay : String): Int
    @Query("SELECT sum(tongTien) FROM HoaDon where trangThai = 'Đã thanh toán' and thoiGianDat > :dauThang and thoiGianDat < :cuoiThang  ")
    fun getDoanhThuTrongThang(dauThang : String,cuoiThang: String): Int
}