// 黑色导航栏

function Navigation() {
  return (
    <div className="navigate">
      {/* 导航栏上方图标 */}
      <div className="top">
        <img src="/photo1.jpg" alt="你" className="photo" />
        <svg className="icon" aria-hidden="true">
          <title>聊天</title>
          <use xlinkHref="#icon-wechat"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>通讯录</title>
          <use xlinkHref="#icon-lianxirenliebiao"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>收藏</title>
          <use xlinkHref="#icon-shoucang"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>聊天文件</title>
          <use xlinkHref="#icon-wenjian1"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>朋友圈</title>
          <use xlinkHref="#icon-pengyouquan"></use>
        </svg>
      </div>
      {/* 导航栏下方图标 */}
      <div className="bottom">
        <svg className="icon" aria-hidden="true">
          <title>小程序面板</title>
          <use xlinkHref="#icon-xiaochengxu"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>手机</title>
          <use xlinkHref="#icon-shouji"></use>
        </svg>
        <svg className="icon" aria-hidden="true">
          <title>设置及其他</title>
          <use xlinkHref="#icon-shezhi"></use>
        </svg>
      </div>
    </div>
  );
}

export default Navigation;
