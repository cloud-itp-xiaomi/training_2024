// 初始化聊天群组数据

export const initialChatGroups = [
  {
    id: 1,
    name: "相亲相爱一家人",
    photo: "/group0.png",
    messages: [
      {
        id: 1,
        text: "早上好！",
        timestamp: "09:00:20",
        sender: "妈妈",
        senderId: 1,
        photo: "/photo2.jpg",
      },
      {
        id: 2,
        text: "妈妈早上好！",
        timestamp: "09:01:18",
        sender: "你",
        senderId: 0,
        photo: "/photo1.jpg",
      },
      {
        id: 3,
        text: "早呀！",
        timestamp: "09:10:30",
        sender: "爸爸",
        senderId: 3,
        photo: "/photo0.jpg",
      },
    ],
  },
  {
    id: 2,
    name: "武汉科技大学跳蚤市场",
    photo: "/group2.png",
    messages: [
      {
        id: 1,
        text: "酒拾烤肉，128代200，叠2！",
        timestamp: "12:10:14",
        sender: "买券找我",
        senderId: 2,
        photo: "/photo3.jpg",
      },
      {
        id: 2,
        text: "1元吃大牌 每人每天可以参加5次哦（同一品当天仅限1次） 1️⃣ 如何1元享点击下方活动卡片→点击想要的商品→点击“我要发起助力”->邀请好友进群→1元购买商品券→在美团外卖app下单使用。 2️⃣ 本期有哪些大牌？┆瑞幸咖啡┆丝绒拿铁┆  华莱士  ┆ 人气畅享三件套┆  茶百道  ┆ 茉莉奶绿【全线始发🔥 多邀多得！】",
        timestamp: "12:20:13",
        sender: "天天神券福利君",
        senderId: 4,
        photo: "/photo5.png",
      },
    ],
  },
  {
    id: 3,
    name: "朋友群",
    photo: "/group1.png",
    messages: [
      {
        id: 1,
        text: "昨晚的球赛真精彩！",
        timestamp: "11:00:56",
        sender: "老王",
        senderId: 6,
        photo: "/photo4.jpg",
      },
      {
        id: 2,
        text: "确实，非常刺激",
        timestamp: "11:03:25",
        sender: "你",
        senderId: 0,
        photo: "/photo1.jpg",
      },
    ],
  },
  {
    id: 4,
    name: "武汉同城吃喝玩乐",
    photo: "/group2.png",
    messages: [],
  },
];
