// 黑色导航栏
import Icon from "./Icon";

function Navigation() {
  return (
    <div className="navigate">
      {/* 导航栏上方图标 */}
      <div className="top">
        <img src="/photo1.jpg" alt="你" className="photo" />
        <Icon iconHref="#icon-wechat" title="聊天" />
        <Icon iconHref="#icon-lianxirenliebiao" title="通讯录" />
        <Icon iconHref="#icon-shoucang" title="收藏" />
        <Icon iconHref="#icon-wenjian1" title="聊天文件" />
        <Icon iconHref="#icon-pengyouquan" title="朋友圈" />
      </div>
      {/* 导航栏下方图标 */}
      <div className="bottom">
        <Icon iconHref="#icon-xiaochengxu" title="小程序面板" />
        <Icon iconHref="#icon-shouji" title="手机" />
        <Icon iconHref="#icon-shezhi" title="设置及其他" />
      </div>
    </div>
  );
}

export default Navigation;
