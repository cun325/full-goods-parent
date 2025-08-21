const WebSocket = require('ws');

// 测试WebSocket连接
function testWebSocketConnection() {
  console.log('正在测试WebSocket连接...');
  
  const ws = new WebSocket('ws://localhost:8080/ws');
  
  ws.on('open', function open() {
    console.log('✅ WebSocket连接成功');
    
    // 发送认证消息
    const authMessage = {
      type: 'auth',
      token: 'test-token'
    };
    
    ws.send(JSON.stringify(authMessage));
    console.log('📤 发送认证消息:', authMessage);
  });
  
  ws.on('message', function message(data) {
    try {
      const parsedData = JSON.parse(data);
      console.log('📥 收到消息:', parsedData);
    } catch (error) {
      console.log('📥 收到原始消息:', data.toString());
    }
  });
  
  ws.on('error', function error(err) {
    console.error('❌ WebSocket错误:', err.message);
  });
  
  ws.on('close', function close() {
    console.log('🔌 WebSocket连接已关闭');
  });
  
  // 5秒后关闭连接
  setTimeout(() => {
    ws.close();
  }, 5000);
}

// 测试消息API
async function testMessageAPI() {
  console.log('\n正在测试消息API...');
  
  try {
    const fetch = require('node-fetch');
    
    // 测试获取消息列表
    const response = await fetch('http://localhost:8080/api/messages/user/1?page=1&size=10', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    if (response.ok) {
      const data = await response.json();
      console.log('✅ 消息API测试成功');
      console.log('📋 响应数据:', JSON.stringify(data, null, 2));
    } else {
      console.error('❌ 消息API测试失败:', response.status, response.statusText);
      const errorText = await response.text();
      console.error('错误详情:', errorText);
    }
  } catch (error) {
    console.error('❌ 消息API测试异常:', error.message);
  }
}

// 运行测试
async function runTests() {
  console.log('🚀 开始测试消息系统...');
  
  // 先测试API
  await testMessageAPI();
  
  // 再测试WebSocket
  testWebSocketConnection();
}

runTests();