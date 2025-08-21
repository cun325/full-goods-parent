#!/bin/bash

# 构建脚本 - 商城项目
# 用于自动化构建和部署流程

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "$1 命令未找到，请先安装"
        exit 1
    fi
}

# 检查必要的工具
check_dependencies() {
    log_info "检查依赖工具..."
    check_command "mvn"
    check_command "npm"
    check_command "docker"
    check_command "docker-compose"
    log_success "依赖检查完成"
}

# 清理构建产物
clean_build() {
    log_info "清理构建产物..."
    
    # 清理Maven构建产物
    mvn clean -q
    
    # 清理前端构建产物
    if [ -d "front-end/shopapp-admin/dist" ]; then
        rm -rf front-end/shopapp-admin/dist
    fi
    
    if [ -d "fruitshop-uniapp/dist" ]; then
        rm -rf fruitshop-uniapp/dist
    fi
    
    log_success "清理完成"
}

# 构建后端服务
build_backend() {
    log_info "构建后端服务..."
    
    # 编译并打包
    mvn clean package -DskipTests -q
    
    if [ $? -eq 0 ]; then
        log_success "后端构建成功"
    else
        log_error "后端构建失败"
        exit 1
    fi
}

# 构建前端项目
build_frontend() {
    log_info "构建前端项目..."
    
    # 构建管理后台
    log_info "构建管理后台..."
    cd front-end/shopapp-admin
    
    if [ ! -d "node_modules" ]; then
        log_info "安装管理后台依赖..."
        npm install
    fi
    
    npm run build
    
    if [ $? -eq 0 ]; then
        log_success "管理后台构建成功"
    else
        log_error "管理后台构建失败"
        exit 1
    fi
    
    cd ../..
    
    # 构建移动端项目
    log_info "构建移动端项目..."
    cd fruitshop-uniapp
    
    if [ ! -d "node_modules" ]; then
        log_info "安装移动端依赖..."
        npm install
    fi
    
    npm run build
    
    if [ $? -eq 0 ]; then
        log_success "移动端构建成功"
    else
        log_error "移动端构建失败"
        exit 1
    fi
    
    cd ..
}

# 构建Docker镜像
build_docker() {
    log_info "构建Docker镜像..."
    
    # 构建所有服务镜像
    docker-compose build --no-cache
    
    if [ $? -eq 0 ]; then
        log_success "Docker镜像构建成功"
    else
        log_error "Docker镜像构建失败"
        exit 1
    fi
}

# 运行测试
run_tests() {
    log_info "运行测试..."
    
    # 运行后端测试
    mvn test -q
    
    if [ $? -eq 0 ]; then
        log_success "测试通过"
    else
        log_warning "测试失败，但继续构建"
    fi
}

# 部署服务
deploy_services() {
    log_info "部署服务..."
    
    # 停止现有服务
    docker-compose down
    
    # 启动所有服务
    docker-compose up -d
    
    if [ $? -eq 0 ]; then
        log_success "服务部署成功"
        log_info "服务访问地址："
        log_info "  - 管理后台: http://localhost/admin"
        log_info "  - API接口: http://localhost/api"
        log_info "  - 图片服务: http://localhost/images"
        log_info "  - 监控面板: http://localhost:3000 (admin/admin123)"
    else
        log_error "服务部署失败"
        exit 1
    fi
}

# 检查服务状态
check_services() {
    log_info "检查服务状态..."
    
    sleep 10  # 等待服务启动
    
    # 检查各个服务
    services=("admin-service:8082" "api-service:8081" "image-service:8083")
    
    for service in "${services[@]}"; do
        name=$(echo $service | cut -d':' -f1)
        port=$(echo $service | cut -d':' -f2)
        
        if docker-compose ps | grep -q "$name.*Up"; then
            log_success "$name 服务运行正常"
        else
            log_error "$name 服务启动失败"
        fi
    done
}

# 显示帮助信息
show_help() {
    echo "商城项目构建脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  all          完整构建和部署流程"
    echo "  clean        清理构建产物"
    echo "  backend      仅构建后端"
    echo "  frontend     仅构建前端"
    echo "  docker       仅构建Docker镜像"
    echo "  test         运行测试"
    echo "  deploy       部署服务"
    echo "  status       检查服务状态"
    echo "  stop         停止所有服务"
    echo "  logs         查看服务日志"
    echo "  help         显示此帮助信息"
    echo ""
}

# 停止服务
stop_services() {
    log_info "停止所有服务..."
    docker-compose down
    log_success "服务已停止"
}

# 查看日志
show_logs() {
    log_info "显示服务日志..."
    docker-compose logs -f
}

# 主函数
main() {
    case "$1" in
        "all")
            check_dependencies
            clean_build
            build_backend
            build_frontend
            run_tests
            build_docker
            deploy_services
            check_services
            ;;
        "clean")
            clean_build
            ;;
        "backend")
            check_dependencies
            build_backend
            ;;
        "frontend")
            check_dependencies
            build_frontend
            ;;
        "docker")
            check_dependencies
            build_docker
            ;;
        "test")
            check_dependencies
            run_tests
            ;;
        "deploy")
            check_dependencies
            deploy_services
            check_services
            ;;
        "status")
            check_services
            ;;
        "stop")
            stop_services
            ;;
        "logs")
            show_logs
            ;;
        "help"|"")
            show_help
            ;;
        *)
            log_error "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"