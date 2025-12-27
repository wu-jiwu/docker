# 基于轻量Nginx镜像
FROM nginx:alpine

# 复制自定义Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制静态页面
COPY static/ /usr/share/nginx/html/

# 健康检查：验证Nginx服务可用性
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget -q -O /dev/null http://localhost:80/health || exit 1

# 暴露端口
EXPOSE 80