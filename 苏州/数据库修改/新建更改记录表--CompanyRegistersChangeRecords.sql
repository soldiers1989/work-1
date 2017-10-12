-- 添加公司记录更改表 字段SN EditTime Editor Content
CREATE TABLE CompanyRegistersChangeRecords
(
    CompanyRegistersChangeRecordId int not null primary key,
    CompanyRegisterId int,
    SN nvarchar(50),
    EditTime datetime,
    Editor nvarchar(100),
    Content nvarchar(Max)
)

