USE [SuzhouEisp]
GO

/****** Object:  Table [dbo].[PatentManages]    Script Date: 2017/10/18 17:55:19 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PatentManages](
    [PatentManageId] [int] IDENTITY(1,1) NOT NULL,
    [PatentName] [nvarchar](1000) NULL,
    [PatentYear] [nvarchar](50) NULL,
    [Invention] [nvarchar](max) NULL,
    [UtilityModel] [nvarchar](max) NULL,
    [PatentDesign] [nvarchar](max) NULL,
    [YearSum] [int] NULL,
    [Remarks] [nvarchar](max) NULL,
    [Creator] [nvarchar](100) NULL,
    [CreatedTime] [datetime] NULL,
    [ModifyName] [nvarchar](100) NULL,
    [ModifyTime] [datetime] NULL,
    [Type] [nvarchar](50) NULL,
 CONSTRAINT [PK_PatentManages] PRIMARY KEY CLUSTERED 
(
    [PatentManageId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO


