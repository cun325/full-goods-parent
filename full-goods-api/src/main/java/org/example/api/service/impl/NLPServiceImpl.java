package org.example.api.service.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.NLPService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NLP服务实现类
 */
@Slf4j
@Service
public class NLPServiceImpl implements NLPService {

    // 水果特性关键词映射
    private static final Map<String, List<String>> FEATURE_KEYWORDS = new HashMap<>();
    
    // 口感关键词映射
    private static final Map<String, List<String>> TASTE_KEYWORDS = new HashMap<>();
    
    // 水果名称关键词
    private static final List<String> FRUIT_NAMES = new ArrayList<>();
    
    // 情感词典
    private static final Map<String, Integer> SENTIMENT_DICT = new HashMap<>();
    
    // 初始化特性关键词和情感词典
  /*  static {
        // 口感相关关键词 - 更详细的分类
        TASTE_KEYWORDS.put("甜", Arrays.asList("甜", "香甜", "清甜", "甜腻", "甜美", "甘甜", "蜜甜", "甜润"));
        TASTE_KEYWORDS.put("酸", Arrays.asList("酸", "酸甜", "微酸", "酸爽", "酸涩", "酸味"));
        TASTE_KEYWORDS.put("脆", Arrays.asList("脆", "爽脆", "清脆", "酥脆"));
        TASTE_KEYWORDS.put("软", Arrays.asList("软", "软糯", "绵软", "柔软"));
        TASTE_KEYWORDS.put("多汁", Arrays.asList("多汁", "汁多", "水分足", "爽口"));
        
        // 口感相关关键词
        FEATURE_KEYWORDS.put("口感", Arrays.asList("甜", "酸", "脆", "软", "多汁", "香甜", "酸甜", "爽口", "清甜", "软糯", "微酸", "酸爽", "蜜甜", "绵软"));
        
        // 适用人群相关关键词
        FEATURE_KEYWORDS.put("人群", Arrays.asList("孕妇", "儿童", "老人", "糖尿病", "高血压", "减肥", "贫血", "孕期", "小孩", "宝宝", "婴儿"));
        
        // 营养相关关键词
        FEATURE_KEYWORDS.put("营养", Arrays.asList("维生素", "纤维", "蛋白质", "铁", "钙", "维C", "抗氧化", "胡萝卜素", "膳食纤维", "电解质", "补血", "美容", "养颜"));
        
        // 产地相关关键词
        FEATURE_KEYWORDS.put("产地", Arrays.asList("国产", "进口", "海南", "新疆", "山东", "泰国", "澳洲", "美国", "广西"));
        
        // 分类相关关键词
        FEATURE_KEYWORDS.put("分类", Arrays.asList("热带", "应季", "水果", "浆果", "柑橘类", "瓜类"));
        
        // 水果名称关键词
        FRUIT_NAMES.addAll(Arrays.asList("樱桃", "椰青", "哈密瓜", "黄桃", "百香果", "荔枝", "苹果", "香蕉", "橙子", "葡萄", "草莓", "猕猴桃", "芒果", "菠萝", "西瓜", "桃子", "梨", "柚子", "柠檬", "火龙果"));
        
        // 初始化情感词典
        SENTIMENT_DICT.put("喜欢", 1);
        SENTIMENT_DICT.put("爱吃", 1);
        SENTIMENT_DICT.put("好吃", 1);
        SENTIMENT_DICT.put("美味", 1);
        SENTIMENT_DICT.put("想要", 1);
        SENTIMENT_DICT.put("需要", 1);
        SENTIMENT_DICT.put("不喜欢", -1);
        SENTIMENT_DICT.put("讨厌", -1);
        SENTIMENT_DICT.put("难吃", -1);
    }
*/
    @Override
    public Map<String, Object> analyzeInput(String input) {
        Map<String, Object> result = new HashMap<>();
        
        // 提取关键词
        List<String> keywords = extractKeywords(input);
        result.put("keywords", keywords);
        
        // 匹配特性
        List<String> features = matchFruitFeatures(input);
        result.put("features", features);
        
        // 分析情感
        String sentiment = analyzeSentiment(input);
        result.put("sentiment", sentiment);
        
        // 提取数字（如价格范围）
        List<Integer> numbers = extractNumbers(input);
        if (!numbers.isEmpty()) {
            result.put("numbers", numbers);
        }
        
        return result;
    }

    @Override
    public List<String> matchFruitFeatures(String input) {
        Set<String> features = new HashSet<>();
        String lowerInput = input.toLowerCase();
        
        // 遍历所有特性关键词
        for (Map.Entry<String, List<String>> entry : FEATURE_KEYWORDS.entrySet()) {
            String featureType = entry.getKey();
            List<String> keywords = entry.getValue();
            
            for (String keyword : keywords) {
                if (lowerInput.contains(keyword.toLowerCase())) {
                    features.add(featureType + "-" + keyword);
                }
            }
        }
        
        return new ArrayList<>(features);
    }

    @Override
    public List<String> extractKeywords(String input) {
        Set<String> keywords = new HashSet<>();
        String lowerInput = input.toLowerCase();
        
        // 1. 直接匹配水果名称
        for (String fruitName : FRUIT_NAMES) {
            if (lowerInput.contains(fruitName.toLowerCase())) {
                keywords.add(fruitName);
            }
        }
        
        // 2. 匹配口感关键词
        for (Map.Entry<String, List<String>> entry : TASTE_KEYWORDS.entrySet()) {
            String tasteType = entry.getKey();
            List<String> tasteWords = entry.getValue();
            
            for (String tasteWord : tasteWords) {
                if (lowerInput.contains(tasteWord.toLowerCase())) {
                    keywords.add(tasteWord);
                    // 同时添加口感类型作为关键词
                    keywords.add(tasteType);
                }
            }
        }
        
        // 3. 匹配其他特性关键词
        for (Map.Entry<String, List<String>> entry : FEATURE_KEYWORDS.entrySet()) {
            List<String> featureWords = entry.getValue();
            
            for (String featureWord : featureWords) {
                if (lowerInput.contains(featureWord.toLowerCase())) {
                    keywords.add(featureWord);
                }
            }
        }
        
        // 4. 使用HanLP进行分词和关键词提取（作为补充）
        try {
            List<Term> terms = HanLP.segment(input);
            for (Term term : terms) {
                // 只保留名词、动词、形容词作为关键词
                String nature = term.nature.toString();
                if (nature.startsWith("n") || nature.startsWith("v") || nature.startsWith("a")) {
                    String word = term.word;
                    // 过滤掉单字符和常见停用词
                    if (word.length() > 1 && !isStopWord(word)) {
                        keywords.add(word);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("HanLP分词失败: {}", e.getMessage());
        }
        
        log.info("输入: {}, 提取关键词: {}", input, keywords);
        return new ArrayList<>(keywords);
    }
    
    /**
     * 判断是否为停用词
     */
    private boolean isStopWord(String word) {
        Set<String> stopWords = new HashSet<>(Arrays.asList("的", "了", "在", "是", "我", "有", "和", "就", "不", "人", "都", "一", "个", "上", "也", "很", "到", "说", "要", "去", "你", "会", "着", "没有", "看", "好", "自己", "这"));
        return stopWords.contains(word.toLowerCase());
    }

    @Override
    public String analyzeSentiment(String input) {
        int score = 0;
        String lowerInput = input.toLowerCase();
        
        // 计算情感得分
        for (Map.Entry<String, Integer> entry : SENTIMENT_DICT.entrySet()) {
            String word = entry.getKey();
            Integer value = entry.getValue();
            
            if (lowerInput.contains(word.toLowerCase())) {
                score += value;
            }
        }
        
        // 根据得分判断情感倾向
        if (score > 0) {
            return "positive";
        } else if (score < 0) {
            return "negative";
        } else {
            return "neutral";
        }
    }
    
    /**
     * 从输入中提取数字
     */
    private List<Integer> extractNumbers(String input) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        
        while (matcher.find()) {
            try {
                numbers.add(Integer.parseInt(matcher.group()));
            } catch (NumberFormatException e) {
                log.warn("提取数字异常: {}", e.getMessage());
            }
        }
        
        return numbers;
    }
}