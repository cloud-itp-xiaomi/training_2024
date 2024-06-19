//将时间转换为总分钟
export const timeToMinutes = (times) => {
  const [hours, minutes, seconds] = times.split(":").map(Number);
  return hours * 3600 + minutes * 60 + seconds;
};

// 判断相隔时间超没超过两分钟
export const judgeTime = (current, prev) => {
  if (!prev) return true;
  const diff = timeToMinutes(current.timestamp) - timeToMinutes(prev.timestamp);
  if (diff > 120) return true;
};

//格式化时间
export const formatTimestamp = (timestamp) => {
  return timestamp.substring(0, 5);
};
