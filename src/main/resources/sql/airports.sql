insert into public.airports(airport_id, airport_name, airport_code, city, country_id)
values
    (1, 'Chhatrapati Shivaji International Airport', 'BOM', 'Mumbai', 95),
    (2, 'Indira Gandhi International Airport', 'DEL', 'New Delhi', 95),
    (3, 'Cochin International Airport', 'COK', 'Kochi', 95),
    (4, 'Sardar Vallabhbhai Patel International Airport', 'AMD', 'Ahmedabad', 95),
    (5, 'Tiruchirappalli International Airport', 'TRZ', 'Tiruchirappalli', 95),
    (6, 'Tokyo International Airport - Haneda Airport', 'HND', 'Tokyo (Haneda)', 105),
    (7, 'Kansai International Airport', 'KIX', 'Osaka', 105),
    (8, 'Narita International Airport', 'NRT', 'Tokyo (Narita)', 105),
    (9, 'Chubu Centrair International Airport', 'NGO', 'Nagoya', 105),
    (10, 'Fukuoka International Airport', 'FUK', 'Fukuoka', 105),
    (11, 'Gimhae International Airport', 'PUS', 'Busan', 200),
    (12, 'Daegu International Airport', 'TAE', 'Daegu', 200),
    (13, 'Incheon International Airport', 'ICN', 'Incheon', 200),
    (15, 'Kaohsiung International Airport', 'KHH', 'Kaohsiung', 211),
    (16, 'Taiwan Taoyuan International Airport', 'TPE', 'Taipei', 211),
    (17, 'Tainan Airport', 'TNN', 'Tainan', 211),
    (18, 'Taichung International Airport', 'RMQ', 'Taichung', 211),
    (19, 'Kuala Lumpur International Airport (KLIA)', 'KUL', 'Kuala Lumpur', 127),
    (20, 'Changi International Airport', 'SIN', 'Singapore', 193),
    (21, 'Soekarno-Hatta International Airport', 'CGK', 'Jakarta', 196),
    (22, 'Ngurah Rai International Airport (Denpasar)', 'DPS', 'Bali', 196),
    (23, 'Siem Reap–Angkor International Airport', 'SAI', 'Siem Reap', 36),  -- Giả sử country_id cho Campuchia là 5
    (24, 'Yangon International Airport', 'RGN', 'Yangon', 144),  -- Giả sử country_id cho Myanmar là 6
    (25, 'Almaty International Airport', 'ALA', 'Almaty', 108),  -- Giả sử country_id cho Kazakhstan là 7
    (26, 'Nursultan Nazarbayev International Airport', 'NQZ', 'Astana', 108),  -- Cũng là Kazakhstan
    (27, 'Vostosny International Airport', 'TAS', 'Tashkent', 231),  -- Giả sử country_id cho Uzbekistan là 8
    (28, 'Melbourne Tullamarine Airport', 'MEL', 'Melbourne', 13),  -- Giả sử country_id cho Úc là 9
    (29, 'Perth International Airport', 'PER', 'Perth', 13),  -- Cũng là Úc
    (30, 'Brisbane International Airport', 'BNE', 'Brisbane', 13),  -- Cũng là Úc
    (31, 'Kingsford Smith Airport', 'SYD', 'Sydney', 13),  -- Cũng là Úc
    (32, 'Suvarnabhumi Airport', 'BKK', 'Bangkok', 214),  -- Giả sử country_id cho Thái Lan là 10
    (33, 'Chiang Mai International Airport', 'CNX', 'Chiang Mai', 214),  -- Cũng là Thái Lan
    (34, 'Chiang Rai International Airport', 'CEI', 'Chiang Rai', 214),  -- Cũng là Thái Lan
    (35, 'Hat Yai International Airport', 'HDY', 'Hat Yai', 214),  -- Cũng là Thái Lan
    (36, 'Khon Kaen Airport', 'KKC', 'Khon Kaen', 214),  -- Cũng là Thái Lan
    (37, 'Krabi International Airport', 'KBV', 'Krabi', 214),  -- Cũng là Thái Lan
    (38, 'Nakhon Si Thammarat Airport', 'NST', 'Nakhon Si Thammarat', 214),  -- Cũng là Thái Lan
    (39, 'Phuket International Airport', 'HKT', 'Phuket', 214),  -- Cũng là Thái Lan
    (40, 'Surat Thani International Airport', 'URT', 'Surat Thani', 214),  -- Cũng là Thái Lan
    (41, 'Ubon Ratchathani Airport', 'UBP', 'Ubon Ratchathani', 214),  -- Cũng là Thái Lan
    (42, 'Udon Thani International Airport', 'UTH', 'Udon Thani', 214),  -- Cũng là Thái Lan
    (43, 'U-Tapao–Rayong–Pattaya International Airport', 'UTP', 'Rayong', 214),
    (44, 'Tan Son Nhat International Airport', 'SGN', 'Ho Chi Minh City', 235),  -- Giả sử country_id cho Việt Nam là 1
    (45, 'Noi Bai International Airport', 'HAN', 'Hanoi City', 235),
    (46, 'Da Nang International Airport', 'DAD', 'Da Nang', 235),
    (47, 'Phu Quoc International Airport', 'PQC', 'Phu Quoc', 235),
    (48, 'Cam Ranh International Airport', 'CXR', 'Nha Trang', 235),
    (49, 'Can Tho International Airport', 'VCA', 'Can Tho', 235),
    (50, 'Lien Khuong International Airport', 'DLI', 'Da Lat', 235),
    (51, 'Cat Bi International Airport', 'HPH', 'Hai Phong', 235),
    (52, 'Phu Bai International Airport', 'HUI', 'Hue', 235),
    (53, 'Van Don International Airport', 'VDO', 'Quang Ninh', 235),
    (54, 'Vinh International Airport', 'VII', 'Nghe An', 235),
    (55, 'Buon Ma Thuot Airport', 'BMV', 'Dak Lak', 235),
    (56, 'Chu Lai Airport', 'VCL', 'Chu Lai', 235),
    (57, 'Dong Hoi Airport', 'VDH', 'Quang Binh', 235),
    (58, 'Phu Cat Airport', 'UIH', 'Binh Dinh', 235),
    (59, 'Pleiku Airport', 'PXU', 'Gia Lai', 235),
    (60, 'Tho Xuan Airport', 'THD', 'Thanh Hoa', 235),
    (61, 'Tuy Hoa Airport', 'TBB', 'Phu Yen', 235),
    (62, 'Hong Kong International Airport', 'HKG', 'Hong Kong', 92),
    (63, 'Macau International Airport', 'MFM', 'Macao', 123),
    (64, 'Hefei Xinqiao International Airport', 'HFE', 'Hefei', 44),  -- Giả sử country_id cho Trung Quốc là 4
    (65, 'Haikou Meilan International Airport', 'HAK', 'Haikou', 44),
    (66, 'Baotou Airport', 'BAV', 'Baotou', 44),
    (67, 'Harbin Taiping International Airport', 'HRB', 'Harbin', 44),
    (68, 'Changsha Huanghua International Airport', 'CSX', 'Changsha', 44),
    (69, 'Kunming Changshui International Airport', 'KMG', 'Kunming', 44),
    (70, 'Dalian Zhoushuizi International Airport', 'DLC', 'Dalian', 44),
    (71, 'Jinan Yaoqiang International Airport', 'TNA', 'Jinan', 44),
    (72, 'Hangzhou Xiaoshan International Airport', 'HGH', 'Hangzhou', 44),
    (73, 'Yinchuan Hedong International Airport', 'INC', 'Hedong', 44),
    (74, 'Baita Hohhot Baita International Airport', 'HET', 'Hohhot', 44),
    (75, 'Lanzhou Zhongchuan International Airport', 'LHW', 'Lanzhou Zhongchuan', 44),
    (76, 'Nanjing Lukou International Airport', 'NKG', 'Nanjing Lukou', 44),
    (77, 'Nantong Xingdong International Airport', 'NTG', 'Nantong', 44),
    (78, 'Nanchang Changbei International Airport', 'KHN', 'Nanchang', 44),
    (79, 'Dongsheng Airport', 'DSN', 'Ordos', 44),
    (80, 'Yiwu Airport', 'YIW', 'Yiwu', 44),
    (81, 'Nanning Wuxu International Airport', 'NNG', 'Nanning Wuxu', 44),
    (82, 'Ningbo Lishe International Airport', 'NGB', 'Ningbo', 44),
    (83, 'Fuzhou Changle International Airport', 'FOC', 'Fuzhou', 44),
    (84, 'Qingdao Liuting International Airport', 'TAO', 'Qingdao', 44),
    (85, 'Guiyang Longdongbao Airport', 'KWE', 'Guiyang', 44),
    (86, 'Xi''an Xianyang International Airport', 'XIY', 'Xi''an', 44),
    (87, 'Shijiazhuang Zhengding International Airport', 'SJW', 'Shijiazhuang', 44),
    (88, 'Taiyuan Wusu International Airport', 'TYN', 'Taiyuan', 44),
    (89, 'Chengdu Shuangliu International Airport', 'CTU', 'Chengdu', 44),
    (90, 'Shenyang Taoxian International Airport', 'SHE', 'Shenyang', 44),
    (91, 'Tianjin Binhai International Airport', 'TSN', 'Tianjin', 44),
    (92, 'Changzhou Benniu Airport', 'CZX', 'Changzhou', 44),
    (93, 'Shanghai Pudong International Airport', 'PVG', 'Shanghai', 44),
    (94, 'Zhengzhou Xinzheng International Airport', 'CGO', 'Zhengzhou', 44),
    (95, 'Chongqing Jiangbei International Airport', 'CKG', 'Chongqing', 44),
    (96, 'Zhangjiajie Hehua Airport', 'DYG', 'Zhangjiajie', 44),
    (97, 'Changchun Longjia International Airport', 'CGQ', 'Changchun', 44),
    (98, 'Quanzhou Jinjiang International Airport', 'JJN', 'Quanzhou', 44),
    (99, 'Sunan Shuofang International Airport', 'WUX', 'Wuxi', 44),
    (100, 'Wuhan Tianhe International Airport', 'WUH', 'Wuhan', 44),
    (101, 'Xuzhou Guanyin Airport', 'XUZ', 'Xuzhou', 44);