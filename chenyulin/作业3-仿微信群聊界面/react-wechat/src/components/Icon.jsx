// 导出iconfont图标

export default function Icon({ iconHref, title, className = "icon" }) {
  return (
    <svg className={className} aria-hidden="true">
      <title>{title}</title>
      <use xlinkHref={iconHref}></use>
    </svg>
  );
}
