CREATE DATABASE EduSys;
use EduSys;

create table NhanVien(
	MaNV	NVARCHAR(20) primary key NOT NULL,
	MatKhau	NVARCHAR(50) NOT NULL,
	HoTen	NVARCHAR(50) NOT NULL,
	VaiTro	BIT Not null
);

create table ChuyenDe(
	MaCD nchar(5) not null,
	TenCD nvarchar(50) not null,
	HocPhi float not null,
	ThoiLuong int not null,
	Hinh nvarchar(50) not null,
	MoTa nvarchar(255) not null,
	primary key(MaCD)
);

create table KhoaHoc(
	MaKH int identity(1,1) not null,
	MaCD nchar(5) not null,
	HocPhi float not null,
	ThoiLuong int not null,
	NgayKG date not null,
	GhiChu nvarchar(50) null,
	MaNV nvarchar(20) not null,
	NgayTao date not null,
	primary key(MaKH),
	FOREIGN KEY(MaCD) REFERENCES ChuyenDe(MaCD) ON DELETE NO ACTION ON UPDATE CASCADE ,
	FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV) ON DELETE NO ACTION ON UPDATE CASCADE
);

create table NguoiHoc(
	MaNH nchar(7) not null,
	HoTen nvarchar(50) not null,
	NgaySinh date not null,
	GioiTinh bit not null,
	DienThoai nvarchar(50) not null,
	Email nvarchar(50) not null,
	GhiChu nvarchar(max) null,
	MaNV nvarchar(20)not null,
	NgayDK date not null,
	primary key(MaNH),
	FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV) ON DELETE NO ACTION ON UPDATE CASCADE
);

create table HocVien(
	MaHV int identity(1,1) not null,
	MaKH int not null,
	MaNH nchar(7) not null,
	Diem float not null,
	primary key(MaHV),	
	FOREIGN KEY(MaNH) REFERENCES NguoiHoc(MaNH) ON UPDATE CASCADE,
	FOREIGN KEY(MaKH) REFERENCES KhoaHoc(MaKH) ON DELETE CASCADE 
);

-- report: MaNH - HoTen - Diem
if object_id('sp_bangdiem') is not null
	drop proc sp_bangdiem
go
create proc sp_BangDiem(@MaKH int)
as begin 
	select 
		nh.MaNH,
		nh.HoTen,
		hv.Diem
	From HocVien hv
		join NguoiHoc nh on nh.MaNH=hv.MaNH
	Where hv.MaKH= @MaKH
	order by hv.Diem DESC
end

-- Report: Ten_CD - So_HV - Thap_nhat - cao_nhat - Trung_binh
if OBJECT_ID('sp_ThongKeDiem') is not null
	drop proc sp_ThongKeDiem
go
create proc sp_ThongKeDiem
as begin
	select
		TenCD ChuyenDe,
		COUNT(MaHV) SoHV,
		Min(Diem) ThapNhat,
		MAX(Diem) CaoNhat,
		AVG(Diem) TrungBinh
	from KhoaHoc kh
		join HocVien hv on kh.MaKH=hv.MaKH
		join ChuyenDe cd on cd.MaCD=kh.MaCD
	group by TenCD
end

-- Report: Ten_CĐ - SO_KH - So_HV - Doanh_Thu - ThapNhat - CaoNhat - TrungBinh
if OBJECT_ID('sp_ThongKeDoanhThu') is not null
	drop proc sp_ThongKeDoanhThu;
go
CREATE PROC sp_ThongKeDoanhThu(@Year INT)
AS BEGIN
	SELECT
		TenCD ChuyenDe,
		COUNT(DISTINCT kh.MaKH) SoKH,
		COUNT(hv.MaHV) SoHV,
		SUM(kh.HocPhi) DoanhThu,
		MIN(kh.HocPhi) ThapNhat,
		MAX(kh.HocPhi) CaoNhat,
		AVG(kh.HocPhi) TrungBinh
	FROM KhoaHoc kh
		JOIN HocVien hv ON kh.MaKH=hv.MaKH
		JOIN ChuyenDe cd ON cd.MaCD=kh.MaCD
	WHERE YEAR(NgayKG) = @Year
	GROUP BY TenCD
END

-- Report: Nam - SoLuong - DauTien - CuoiCung
if OBJECT_ID('sp_ThongKeNguoiHoc') is not null
	drop proc sp_ThongKeNguoiHoc;
go
CREATE PROC sp_ThongKeNguoiHoc
AS BEGIN
	SELECT
		YEAR(NgayDK) Nam,
		COUNT(*) SoLuong,
		MIN(NgayDK) DauTien,
		MAX(NgayDK) CuoiCung
	FROM NguoiHoc
	GROUP BY YEAR(NgayDK)
END

insert into Nhanvien values('admin', '123', 'admin', 1);

INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV01', N'Lập trình Java cơ bản', 300, 90, N'GAME.png', N'JAV01 - Lập trình Java cơ bản')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV02', N'Lập trình Java nâng cao', 300, 90, N'HTCS.jpg', N'JAV02 - Lập trình Java nâng cao')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV03', N'Lập trình mạng với Java', 200, 70, N'INMA.jpg', N'JAV03 - Lập trình mạng với Java')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV04', N'Lập trình desktop với Swing', 200, 70, N'LAYO.jpg', N'JAV04 - Lập trình desktop với Swing')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO01', N'Dự án với công nghệ MS.NET MVC', 300, 90, N'MOWE.png', N'PRO01 - Dự án với công nghệ MS.NET MVC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO02', N'Dự án với công nghệ Spring MVC', 300, 90, N'Subject.png', N'PRO02 - Dự án với công nghệ Spring MVC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO03', N'Dự án với công nghệ Servlet/JSP', 300, 90, N'GAME.png', N'PRO03 - Dự án với công nghệ Servlet/JSP')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO04', N'Dự án với AngularJS & WebAPI', 300, 90, N'HTCS.jpg', N'PRO04 - Dự án với AngularJS & WebAPI')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO05', N'Dự án với Swing & JDBC', 300, 90, N'INMA.jpg', N'PRO05 - Dự án với Swing & JDBC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO06', N'Dự án với WindowForm', 300, 90, N'LAYO.jpg', N'PRO06 - Dự án với WindowForm')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB01', N'Cơ sở dữ liệu SQL Server', 100, 50, N'MOWE.png', N'RDB01 - Cơ sở dữ liệu SQL Server')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB02', N'Lập trình JDBC', 150, 60, N'Subject.png', N'RDB02 - Lập trình JDBC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB03', N'Lập trình cơ sở dữ liệu Hibernate', 250, 80, N'GAME.png', N'RDB03 - Lập trình cơ sở dữ liệu Hibernate')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER01', N'Lập trình web với Servlet/JSP', 350, 100, N'HTCS.jpg', N'SER01 - Lập trình web với Servlet/JSP')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER02', N'Lập trình Spring MVC', 400, 110, N'INMA.jpg', N'SER02 - Lập trình Spring MVC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER03', N'Lập trình MS.NET MVC', 400, 110, N'LAYO.jpg', N'SER03 - Lập trình MS.NET MVC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER04', N'Xây dựng Web API với Spring MVC & ASP.NET MVC', 200, 70, N'MOWE.png', N'SER04 - Xây dựng Web API với Spring MVC & ASP.NET MVC')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB01', N'Thiết kế web với HTML và CSS', 200, 70, N'Subject.png', N'WEB01 - Thiết kế web với HTML và CSS')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB02', N'Thiết kế web với Bootstrap', 0, 40, N'GAME.png', N'WEB02 - Thiết kế web với Bootstrap')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB03', N'Lập trình front-end với JavaScript và jQuery', 150, 60, N'HTCS.jpg', N'WEB03 - Lập trình front-end với JavaScript và jQuery')
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB04', N'Lập trình AngularJS', 250, 80, N'INMA.jpg', N'WEB04 - Lập trình AngularJS')
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'NoPT', N'123456', N'Phạm Thị Nở', 0)
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'PheoNC', N'123456', N'Nguyễn Chí Phèo', 0)
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'TeoNV', N'123456', N'Nguyễn Văn Tèo', 1)
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS01638', N'LỮ HUY CƯỜNG', CAST(0xAF170B00 AS Date), 1, N'0928768265', N'PS01638@fpt.edu.vn', N'0928768265 - LỮ HUY CƯỜNG', N'PheoNC', CAST(0xAF170B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02037', N'ĐỖ VĂN MINH', CAST(0xC6190B00 AS Date), 1, N'0968095685', N'PS02037@fpt.edu.vn', N'0968095685 - ĐỖ VĂN MINH', N'PheoNC', CAST(0xC6190B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02771', N'NGUYỄN TẤN HIẾU', CAST(0x2E220B00 AS Date), 1, N'0927594734', N'PS02771@fpt.edu.vn', N'0927594734 - NGUYỄN TẤN HIẾU', N'PheoNC', CAST(0x2E220B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02867', N'NGUYỄN HỮU TRÍ', CAST(0xEB200B00 AS Date), 1, N'0946984711', N'PS02867@fpt.edu.vn', N'0946984711 - NGUYỄN HỮU TRÍ', N'TeoNV', CAST(0xEB200B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02930', N'TRẦN VĂN NAM', CAST(0xA1240B00 AS Date), 1, N'0924774498', N'PS02930@fpt.edu.vn', N'0924774498 - TRẦN VĂN NAM', N'TeoNV', CAST(0xA1240B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02979', N'ĐOÀN TRẦN NHẬT VŨ', CAST(0x671C0B00 AS Date), 1, N'0912374818', N'PS02979@fpt.edu.vn', N'0912374818 - ĐOÀN TRẦN NHẬT VŨ', N'TeoNV', CAST(0x671C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02983', N'NGUYỄN HOÀNG THIÊN PHƯỚC', CAST(0x681A0B00 AS Date), 1, N'0912499836', N'PS02983@fpt.edu.vn', N'0912499836 - NGUYỄN HOÀNG THIÊN PHƯỚC', N'PheoNC', CAST(0x681A0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS02988', N'HỒ HỮU HẬU', CAST(0x311A0B00 AS Date), 1, N'0924984876', N'PS02988@fpt.edu.vn', N'0924984876 - HỒ HỮU HẬU', N'PheoNC', CAST(0x311A0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03031', N'PHAN TẤN VIỆT', CAST(0x21160B00 AS Date), 1, N'0924832716', N'PS03031@fpt.edu.vn', N'0924832716 - PHAN TẤN VIỆT', N'PheoNC', CAST(0x21160B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03046', N'NGUYỄN CAO PHƯỚC', CAST(0xDE150B00 AS Date), 1, N'0977117727', N'PS03046@fpt.edu.vn', N'0977117727 - NGUYỄN CAO PHƯỚC', N'PheoNC', CAST(0xDE150B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03080', N'HUỲNH THANH HUY', CAST(0x701C0B00 AS Date), 1, N'0916436052', N'PS03080@fpt.edu.vn', N'0916436052 - HUỲNH THANH HUY', N'PheoNC', CAST(0x701C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03088', N'NGUYỄN HOÀNG TRUNG', CAST(0x24180B00 AS Date), 1, N'0938101529', N'PS03088@fpt.edu.vn', N'0938101529 - NGUYỄN HOÀNG TRUNG', N'PheoNC', CAST(0x24180B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03096', N'ĐOÀN HỮU KHANG', CAST(0xAB1B0B00 AS Date), 1, N'0945196719', N'PS03096@fpt.edu.vn', N'0945196719 - ĐOÀN HỮU KHANG', N'PheoNC', CAST(0xAB1B0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03104', N'LÊ THÀNH PHƯƠNG', CAST(0x3E1A0B00 AS Date), 1, N'0922948096', N'PS03104@fpt.edu.vn', N'0922948096 - LÊ THÀNH PHƯƠNG', N'PheoNC', CAST(0x3E1A0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03120', N'PHẠM NGỌC NHẬT TRƯỜNG', CAST(0x48230B00 AS Date), 1, N'0994296169', N'PS03120@fpt.edu.vn', N'0994296169 - PHẠM NGỌC NHẬT TRƯỜNG', N'PheoNC', CAST(0x48230B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03130', N'ĐẶNG BẢO VIỆT', CAST(0xEF150B00 AS Date), 1, N'0917749344', N'PS03130@fpt.edu.vn', N'0917749344 - ĐẶNG BẢO VIỆT', N'PheoNC', CAST(0xEF150B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03134', N'LÊ DUY BẢO', CAST(0x2E1F0B00 AS Date), 1, N'0926714368', N'PS03134@fpt.edu.vn', N'0926714368 - LÊ DUY BẢO', N'PheoNC', CAST(0x2E1F0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03172', N'NGUYỄN ANH TUẤN', CAST(0xCA180B00 AS Date), 1, N'0920020472', N'PS03172@fpt.edu.vn', N'0920020472 - NGUYỄN ANH TUẤN', N'PheoNC', CAST(0xCA180B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03202', N'PHAN QUỐC QUI', CAST(0x741E0B00 AS Date), 1, N'0930649274', N'PS03202@fpt.edu.vn', N'0930649274 - PHAN QUỐC QUI', N'PheoNC', CAST(0x741E0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03203', N'ĐẶNG LÊ QUANG VINH', CAST(0xC4150B00 AS Date), 1, N'0920197355', N'PS03203@fpt.edu.vn', N'0920197355 - ĐẶNG LÊ QUANG VINH', N'PheoNC', CAST(0xC4150B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03205', N'NGUYỄN MINH SANG', CAST(0x5E1D0B00 AS Date), 1, N'0967006218', N'PS03205@fpt.edu.vn', N'0967006218 - NGUYỄN MINH SANG', N'PheoNC', CAST(0x5E1D0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03222', N'TRẦM MINH MẪN', CAST(0xE71F0B00 AS Date), 1, N'0911183649', N'PS03222@fpt.edu.vn', N'0911183649 - TRẦM MINH MẪN', N'PheoNC', CAST(0xE71F0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03230', N'NGUYỄN PHẠM MINH HÂN', CAST(0x26250B00 AS Date), 1, N'0983469892', N'PS03230@fpt.edu.vn', N'0983469892 - NGUYỄN PHẠM MINH HÂN', N'PheoNC', CAST(0x26250B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03233', N'LƯU KIM HOÀNG DUY', CAST(0x0B1F0B00 AS Date), 1, N'0938357735', N'PS03233@fpt.edu.vn', N'0938357735 - LƯU KIM HOÀNG DUY', N'PheoNC', CAST(0x0B1F0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03251', N'TRẦN QUANG VŨ', CAST(0x4C240B00 AS Date), 1, N'0914531913', N'PS03251@fpt.edu.vn', N'0914531913 - TRẦN QUANG VŨ', N'PheoNC', CAST(0x4C240B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03304', N'BÙI NGỌC THUẬN', CAST(0xFE1C0B00 AS Date), 1, N'0999794115', N'PS03304@fpt.edu.vn', N'0999794115 - BÙI NGỌC THUẬN', N'PheoNC', CAST(0xFE1C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03306', N'HỒ VĂN HÀNH', CAST(0x06190B00 AS Date), 1, N'0912277868', N'PS03306@fpt.edu.vn', N'0912277868 - HỒ VĂN HÀNH', N'PheoNC', CAST(0x06190B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03308', N'TRẦN VIẾT HÙNG', CAST(0xD0220B00 AS Date), 1, N'0916050164', N'PS03308@fpt.edu.vn', N'0916050164 - TRẦN VIẾT HÙNG', N'PheoNC', CAST(0xD0220B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03325', N'NGUYỄN HOÀNG MINH ĐỨC', CAST(0xAB1F0B00 AS Date), 1, N'0912234437', N'PS03325@fpt.edu.vn', N'0912234437 - NGUYỄN HOÀNG MINH ĐỨC', N'PheoNC', CAST(0xAB1F0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03346', N'PHAN THANH PHONG', CAST(0xD7150B00 AS Date), 1, N'0937891282', N'PS03346@fpt.edu.vn', N'0937891282 - PHAN THANH PHONG', N'PheoNC', CAST(0xD7150B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03383', N'TRẦN VŨ LUÂN', CAST(0x8E210B00 AS Date), 1, N'0962030316', N'PS03383@fpt.edu.vn', N'0962030316 - TRẦN VŨ LUÂN', N'PheoNC', CAST(0x8E210B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03389', N'VŨ ĐỨC TUẤN', CAST(0x411A0B00 AS Date), 1, N'0911637415', N'PS03389@fpt.edu.vn', N'0911637415 - VŨ ĐỨC TUẤN', N'PheoNC', CAST(0x411A0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03410', N'TRẦN  NHẠT', CAST(0x3C190B00 AS Date), 1, N'0946219377', N'PS03410@fpt.edu.vn', N'0946219377 - TRẦN  NHẠT', N'PheoNC', CAST(0x3C190B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03411', N'TRƯƠNG THÀNH ĐẠT', CAST(0x3F1B0B00 AS Date), 1, N'0991509408', N'PS03411@fpt.edu.vn', N'0991509408 - TRƯƠNG THÀNH ĐẠT', N'PheoNC', CAST(0x3F1B0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03425', N'TÔ VĂN NĂNG', CAST(0x6E190B00 AS Date), 1, N'0915134551', N'PS03425@fpt.edu.vn', N'0915134551 - TÔ VĂN NĂNG', N'PheoNC', CAST(0x6E190B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03454', N'NGUYỄN NHẬT VĨNH', CAST(0x57230B00 AS Date), 1, N'0917886371', N'PS03454@fpt.edu.vn', N'0917886371 - NGUYỄN NHẬT VĨNH', N'PheoNC', CAST(0x57230B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03472', N'NGUYỄN VĂN HUY', CAST(0xD8150B00 AS Date), 1, N'0960620997', N'PS03472@fpt.edu.vn', N'0960620997 - NGUYỄN VĂN HUY', N'PheoNC', CAST(0xD8150B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03488', N'NGUYỄN NHƯ NGỌC', CAST(0x651D0B00 AS Date), 0, N'0912880267', N'PS03488@fpt.edu.vn', N'0912880267 - NGUYỄN NHƯ NGỌC', N'PheoNC', CAST(0x651D0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03530', N'PHẠM THÀNH TÂM', CAST(0x4D240B00 AS Date), 1, N'0918161783', N'PS03530@fpt.edu.vn', N'0918161783 - PHẠM THÀNH TÂM', N'PheoNC', CAST(0x4D240B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03553', N'ĐINH TẤN CÔNG', CAST(0xEA240B00 AS Date), 1, N'0918182551', N'PS03553@fpt.edu.vn', N'0918182551 - ĐINH TẤN CÔNG', N'PheoNC', CAST(0xEA240B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03561', N'LÊ MINH ĐIỀN', CAST(0xE91C0B00 AS Date), 1, N'0948199564', N'PS03561@fpt.edu.vn', N'0948199564 - LÊ MINH ĐIỀN', N'PheoNC', CAST(0xE91C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03596', N'NGUYỄN THANH HIỀN', CAST(0xED170B00 AS Date), 1, N'0910545901', N'PS03596@fpt.edu.vn', N'0910545901 - NGUYỄN THANH HIỀN', N'PheoNC', CAST(0xED170B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03603', N'LÊ PHẠM KIM THANH', CAST(0x501C0B00 AS Date), 0, N'0924696779', N'PS03603@fpt.edu.vn', N'0924696779 - LÊ PHẠM KIM THANH', N'PheoNC', CAST(0x501C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03610', N'TRẦN ĐÌNH TRƯỜNG', CAST(0xF41C0B00 AS Date), 1, N'0941528106', N'PS03610@fpt.edu.vn', N'0941528106 - TRẦN ĐÌNH TRƯỜNG', N'PheoNC', CAST(0xF41C0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03614', N'NGUYỄN VĂN SÁU', CAST(0x37160B00 AS Date), 1, N'0940711328', N'PS03614@fpt.edu.vn', N'0940711328 - NGUYỄN VĂN SÁU', N'PheoNC', CAST(0x37160B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03618', N'PHÍ ĐÌNH VIỆT HÙNG', CAST(0xA91F0B00 AS Date), 1, N'0939020097', N'PS03618@fpt.edu.vn', N'0939020097 - PHÍ ĐÌNH VIỆT HÙNG', N'PheoNC', CAST(0xA91F0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03638', N'PHẠM NHẬT MINH', CAST(0x86200B00 AS Date), 1, N'0927771672', N'PS03638@fpt.edu.vn', N'0927771672 - PHẠM NHẬT MINH', N'PheoNC', CAST(0x86200B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03640', N'LƯU THANH NGỌC', CAST(0x591B0B00 AS Date), 0, N'0918358164', N'PS03640@fpt.edu.vn', N'0918358164 - LƯU THANH NGỌC', N'PheoNC', CAST(0x591B0B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03662', N'NGUYỄN CAO NGỌC LỢI', CAST(0x34160B00 AS Date), 1, N'0930260679', N'PS03662@fpt.edu.vn', N'0930260679 - NGUYỄN CAO NGỌC LỢI', N'PheoNC', CAST(0x34160B00 AS Date))
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS03674', N'TRẦN TUẤN ANH', CAST(0xF41E0B00 AS Date), 1, N'0914082094', N'PS03674@fpt.edu.vn', N'0914082094 - TRẦN TUẤN ANH', N'PheoNC', CAST(0xF41E0B00 AS Date))

INSERT [dbo].[KhoaHoc] ( [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES ( N'PRO02', 300, 90, CAST(0xBF3D0B00 AS Date), N'', N'TeoNV', CAST(0xB53D0B00 AS Date))
INSERT [dbo].[KhoaHoc] ( [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES ( N'PRO03', 300, 90, CAST(0xBF3D0B00 AS Date), N'', N'TeoNV', CAST(0xB53D0B00 AS Date))
INSERT [dbo].[KhoaHoc] ( [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES ( N'RDB01', 100, 50, CAST(0xBF3D0B00 AS Date), N'', N'TeoNV', CAST(0xB53D0B00 AS Date))
INSERT [dbo].[KhoaHoc] ( [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES ( N'JAV01', 250, 80, CAST(0xBF3D0B00 AS Date), N'', N'TeoNV', CAST(0xB53D0B00 AS Date))

INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS01638', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS02037', -1)
INSERT [dbo].[HocVien] ([MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS02771', -1)
INSERT [dbo].[HocVien] ([MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS02930', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS02983', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 2, N'PS02988', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 3, N'PS01638', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 3, N'PS02037', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 3, N'PS02771', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 3, N'PS02867', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 3, N'PS02930', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS01638', 8)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02037', 9)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02867', 3)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02930', 7)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02771', 8)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02979', 4)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES (1, N'PS02983', 6)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS02988', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03031', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03046', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03080', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03088', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03096', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03104', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03120', -1)
INSERT [dbo].[HocVien] ( [MaKH], [MaNH], [Diem]) VALUES ( 1, N'PS03130', -1)