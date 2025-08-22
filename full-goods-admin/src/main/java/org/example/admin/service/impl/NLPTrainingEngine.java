package org.example.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * NLP训练引擎 - 实现基础的文本分类和处理功能
 */
@Slf4j
@Component
public class NLPTrainingEngine implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final ExecutorService trainingExecutor = Executors.newFixedThreadPool(2);
    private final Map<Long, TrainingTask> runningTasks = new ConcurrentHashMap<>();
    private final Map<String, Integer> vocabulary = new ConcurrentHashMap<>();
    private final Pattern textCleanPattern = Pattern.compile("[^\\u4e00-\\u9fa5a-zA-Z0-9\\s]");

    /**
     * 训练任务类
     */
    public static class TrainingTask implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String name;
        private String modelType;
        private String status;
        private int progress;
        private int currentEpoch;
        private int totalEpochs;
        private double currentLoss;
        private double currentAccuracy; // 确保是double类型
        private List<String> logs;
        private transient Future<?> future; // 不序列化Future对象
        private long startTime;
        private long endTime;
    
        /**
         * 用于反序列化的无参构造函数
         */
        public TrainingTask() {
            this.logs = new ArrayList<>();
        }
    
        public TrainingTask(Long id, String name, String modelType, int totalEpochs) {
            this.id = id;
            this.name = name;
            this.modelType = modelType;
            this.totalEpochs = totalEpochs;
            this.status = "pending";
            this.progress = 0;
            this.currentEpoch = 0;
            this.currentLoss = 1.0;
            this.currentAccuracy = 0.0; // 初始化为0.0
            this.logs = new ArrayList<>();
            this.startTime = System.currentTimeMillis();
        }
    
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getModelType() { return modelType; }
        public void setModelType(String modelType) { this.modelType = modelType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public int getCurrentEpoch() { return currentEpoch; }
        public void setCurrentEpoch(int currentEpoch) { this.currentEpoch = currentEpoch; }
        public int getTotalEpochs() { return totalEpochs; }
        public void setTotalEpochs(int totalEpochs) { this.totalEpochs = totalEpochs; }
        public double getCurrentLoss() { return currentLoss; }
        public void setCurrentLoss(double currentLoss) { this.currentLoss = currentLoss; }
        public double getCurrentAccuracy() { return currentAccuracy; }
        public void setCurrentAccuracy(double currentAccuracy) { this.currentAccuracy = currentAccuracy; }
        public List<String> getLogs() { return logs; }
        public void setLogs(List<String> logs) { this.logs = logs; }
        public Future<?> getFuture() { return future; }
        public void setFuture(Future<?> future) { this.future = future; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
    
        public void addLog(String level, String message) {
            String timestamp = new Date().toString();
            String logEntry = String.format("%s [%s] %s", timestamp, level, message);
            logs.add(logEntry);
            log.info("Task {}: {}", id, logEntry);
        }
    }

    /**
     * 数据集类
     */
    public static class Dataset {
        private List<TextSample> samples;
        private Set<String> labels;
        private Map<String, Integer> labelToIndex;
        
        public Dataset() {
            this.samples = new ArrayList<>();
            this.labels = new HashSet<>();
            this.labelToIndex = new HashMap<>();
        }
        
        public void addSample(String text, String label) {
            samples.add(new TextSample(text, label));
            labels.add(label);
        }
        
        public void buildLabelIndex() {
            int index = 0;
            for (String label : labels) {
                labelToIndex.put(label, index++);
            }
        }
        
        public List<TextSample> getSamples() { return samples; }
        public Set<String> getLabels() { return labels; }
        public Map<String, Integer> getLabelToIndex() { return labelToIndex; }
        public int getNumClasses() { return labels.size(); }
    }

    /**
     * 文本样本类
     */
    public static class TextSample {
        private String text;
        private String label;
        private List<String> tokens;
        private Map<String, Double> features;
        
        public TextSample(String text, String label) {
            this.text = text;
            this.label = label;
            this.features = new HashMap<>();
        }
        
        public String getText() { return text; }
        public String getLabel() { return label; }
        public List<String> getTokens() { return tokens; }
        public void setTokens(List<String> tokens) { this.tokens = tokens; }
        public Map<String, Double> getFeatures() { return features; }
        public void setFeatures(Map<String, Double> features) { this.features = features; }
    }

    /**
     * 水果推荐分类器
     */
    public static class FruitRecommendationClassifier {
        private Map<String, Double> fruitScores;
        private Map<String, Map<String, Double>> userPreferences;
        private Map<String, Map<String, Double>> fruitFeatures;
        
        public FruitRecommendationClassifier() {
            this.fruitScores = new HashMap<>();
            this.userPreferences = new HashMap<>();
            this.fruitFeatures = new HashMap<>();
        }
        
        public void train(Dataset dataset) {
            // 训练水果推荐模型
            for (TextSample sample : dataset.getSamples()) {
                String[] parts = sample.getText().split(",");
                if (parts.length >= 15) {
                    String userId = parts[0];
                    String fruitName = parts[6];
                    double recommendationScore = Double.parseDouble(parts[14]);
                    
                    // 更新水果评分
                    fruitScores.put(fruitName, fruitScores.getOrDefault(fruitName, 0.0) + recommendationScore);
                    
                    // 更新用户偏好
                    userPreferences.computeIfAbsent(userId, k -> new HashMap<>())
                        .put(fruitName, recommendationScore);
                    
                    // 更新水果特征
                    Map<String, Double> features = new HashMap<>();
                    features.put("sweetness", Double.parseDouble(parts[7]));
                    features.put("acidity", Double.parseDouble(parts[8]));
                    features.put("price", Double.parseDouble(parts[11]));
                    features.put("nutrition", Double.parseDouble(parts[12]));
                    fruitFeatures.put(fruitName, features);
                }
            }
        }
        
        public List<String> recommend(String userId, int topK) {
            Map<String, Double> scores = new HashMap<>();
            
            for (String fruit : fruitScores.keySet()) {
                double score = calculateRecommendationScore(userId, fruit);
                scores.put(fruit, score);
            }
            
            return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        }
        
        private double calculateRecommendationScore(String userId, String fruit) {
            double baseScore = fruitScores.getOrDefault(fruit, 0.0);
            double userScore = userPreferences.getOrDefault(userId, new HashMap<>())
                .getOrDefault(fruit, 5.0); // 默认评分
            
            Map<String, Double> features = fruitFeatures.get(fruit);
            double featureScore = 0.0;
            if (features != null) {
                featureScore = features.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            }
            
            return (baseScore * 0.4 + userScore * 0.4 + featureScore * 0.2) / 3.0;
        }
    }
    
    /**
     * 朴素贝叶斯分类器
     */
    public static class NaiveBayesClassifier {
        private Map<String, Double> classProbabilities;
        private Map<String, Map<String, Double>> featureProbabilities;
        private Set<String> vocabulary;
        
        public NaiveBayesClassifier() {
            this.classProbabilities = new HashMap<>();
            this.featureProbabilities = new HashMap<>();
            this.vocabulary = new HashSet<>();
        }
        
        public void train(Dataset dataset) {
            // 计算类别概率
            Map<String, Integer> classCounts = new HashMap<>();
            for (TextSample sample : dataset.getSamples()) {
                classCounts.merge(sample.getLabel(), 1, Integer::sum);
                vocabulary.addAll(sample.getTokens());
            }
            
            int totalSamples = dataset.getSamples().size();
            for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
                classProbabilities.put(entry.getKey(), (double) entry.getValue() / totalSamples);
            }
            
            // 计算特征概率
            for (String label : dataset.getLabels()) {
                Map<String, Double> wordCounts = new HashMap<>();
                int totalWords = 0;
                
                for (TextSample sample : dataset.getSamples()) {
                    if (sample.getLabel().equals(label)) {
                        for (String token : sample.getTokens()) {
                            wordCounts.merge(token, 1.0, Double::sum);
                            totalWords++;
                        }
                    }
                }
                
                Map<String, Double> wordProbs = new HashMap<>();
                for (String word : vocabulary) {
                    // 拉普拉斯平滑
                    double count = wordCounts.getOrDefault(word, 0.0);
                    wordProbs.put(word, (count + 1.0) / (totalWords + vocabulary.size()));
                }
                featureProbabilities.put(label, wordProbs);
            }
        }
        
        public Map<String, Double> predict(List<String> tokens) {
            Map<String, Double> scores = new HashMap<>();
            
            for (String label : classProbabilities.keySet()) {
                double score = Math.log(classProbabilities.get(label));
                Map<String, Double> wordProbs = featureProbabilities.get(label);
                
                for (String token : tokens) {
                    if (wordProbs.containsKey(token)) {
                        score += Math.log(wordProbs.get(token));
                    }
                }
                scores.put(label, score);
            }
            
            return scores;
        }
        
        public String classify(List<String> tokens) {
            Map<String, Double> scores = predict(tokens);
            return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("unknown");
        }
    }

    /**
     * 开始训练任务
     */
    public TrainingTask startTraining(Long taskId, String taskName, String modelType, 
                                    Dataset dataset, Map<String, Object> config) {
        
        int epochs = (Integer) config.getOrDefault("epochs", 10);
        int batchSize = (Integer) config.getOrDefault("batchSize", 32);
        double learningRate = (Double) config.getOrDefault("learningRate", 0.001);
        
        TrainingTask task = new TrainingTask(taskId, taskName, modelType, epochs);
        runningTasks.put(taskId, task);
        
        task.addLog("INFO", "开始训练任务: " + taskName);
        task.addLog("INFO", String.format("配置: epochs=%d, batchSize=%d, learningRate=%.4f", 
                                         epochs, batchSize, learningRate));
        
        Future<?> future = trainingExecutor.submit(() -> {
            try {
                executeTraining(task, dataset, config);
            } catch (Exception e) {
                task.setStatus("failed");
                task.setEndTime(System.currentTimeMillis());
                task.addLog("ERROR", "训练失败: " + e.getMessage());
                log.error("Training failed for task {}", taskId, e);
            }
        });
        
        task.setFuture(future);
        task.setStatus("training");
        
        return task;
    }

    /**
     * 执行训练过程
     */
    private void executeTraining(TrainingTask task, Dataset dataset, Map<String, Object> config) {
        task.addLog("INFO", "开始数据预处理...");
        
        // 数据预处理
        preprocessDataset(dataset);
        task.addLog("INFO", String.format("数据预处理完成，共%d个样本，%d个类别", 
                                         dataset.getSamples().size(), dataset.getNumClasses()));
        
        // 划分训练集和验证集
        List<TextSample> samples = new ArrayList<>(dataset.getSamples());
        Collections.shuffle(samples);
        int trainSize = (int) (samples.size() * 0.8);
        List<TextSample> trainSamples = samples.subList(0, trainSize);
        List<TextSample> validSamples = samples.subList(trainSize, samples.size());
        
        task.addLog("INFO", String.format("数据划分完成，训练集%d个样本，验证集%d个样本", 
                                         trainSamples.size(), validSamples.size()));
        
        // 根据模型类型创建分类器
        Object classifier;
        if ("fruit_recommendation".equals(task.getModelType())) {
            classifier = new FruitRecommendationClassifier();
        } else {
            classifier = new NaiveBayesClassifier();
        }
        
        // 模拟训练过程
        for (int epoch = 1; epoch <= task.getTotalEpochs(); epoch++) {
            if (task.getStatus().equals("stopped")) {
                task.setEndTime(System.currentTimeMillis());
                task.addLog("INFO", "训练已停止");
                return;
            }
            
            task.setCurrentEpoch(epoch);
            task.setProgress((int) ((double) epoch / task.getTotalEpochs() * 100));
            
            // 训练分类器
            Dataset trainDataset = new Dataset();
            for (TextSample sample : trainSamples) {
                trainDataset.addSample(sample.getText(), sample.getLabel());
            }
            trainDataset.buildLabelIndex();
            
            double accuracy;
            if (classifier instanceof FruitRecommendationClassifier) {
                ((FruitRecommendationClassifier) classifier).train(trainDataset);
                accuracy = evaluateFruitRecommendationClassifier((FruitRecommendationClassifier) classifier, validSamples);
            } else {
                ((NaiveBayesClassifier) classifier).train(trainDataset);
                accuracy = evaluateClassifier((NaiveBayesClassifier) classifier, validSamples);
            }
            double loss = 1.0 - accuracy; // 简化的损失计算
            
            task.setCurrentAccuracy(accuracy);
            task.setCurrentLoss(loss);
            
            task.addLog("INFO", String.format("Epoch %d/%d - Loss: %.4f, Accuracy: %.4f", 
                                             epoch, task.getTotalEpochs(), loss, accuracy));
            
            // 模拟训练时间
            try {
                Thread.sleep(2000); // 每个epoch等待2秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                task.setStatus("stopped");
                return;
            }
        }
        
        task.setStatus("completed");
        task.setProgress(100);
        task.setEndTime(System.currentTimeMillis());
        task.addLog("INFO", "训练完成！");
        
        // 保存模型（模拟）
        saveModel(task, classifier);
    }

    /**
     * 对文本进行分词
     * 
     * @param text 输入文本
     * @return 分词结果
     */
    public List<String> tokenize(String text) {
        // 文本清理
        String cleanText = textCleanPattern.matcher(text).replaceAll(" ");
        
        // 简单分词（按空格分割）
        List<String> tokens = Arrays.stream(cleanText.toLowerCase().split("\\s+"))
            .filter(token -> !token.trim().isEmpty())
            .filter(token -> token.length() > 1) // 过滤单字符
            .collect(Collectors.toList());
            
        return tokens;
    }
    
    /**
     * 数据预处理
     */
    private void preprocessDataset(Dataset dataset) {
        for (TextSample sample : dataset.getSamples()) {
            // 文本清理
            String cleanText = textCleanPattern.matcher(sample.getText()).replaceAll(" ");
            
            // 简单分词（按空格分割）
            List<String> tokens = Arrays.stream(cleanText.toLowerCase().split("\\s+"))
                .filter(token -> !token.trim().isEmpty())
                .filter(token -> token.length() > 1) // 过滤单字符
                .collect(Collectors.toList());
            
            sample.setTokens(tokens);
            
            // 构建词汇表
            for (String token : tokens) {
                vocabulary.merge(token, 1, Integer::sum);
            }
        }
        
        dataset.buildLabelIndex();
    }

    /**
     * 评估分类器
     */
    private double evaluateClassifier(NaiveBayesClassifier classifier, List<TextSample> samples) {
        int correct = 0;
        for (TextSample sample : samples) {
            String predicted = classifier.classify(sample.getTokens());
            if (predicted.equals(sample.getLabel())) {
                correct++;
            }
        }
        return (double) correct / samples.size();
    }

    /**
     * 评估水果推荐分类器性能
     */
    private double evaluateFruitRecommendationClassifier(FruitRecommendationClassifier classifier, List<TextSample> samples) {
        int correct = 0;
        for (TextSample sample : samples) {
            String[] parts = sample.getText().split(",");
            if (parts.length >= 15) {
                String userId = parts[0];
                String actualFruit = parts[6];
                
                List<String> recommendations = classifier.recommend(userId, 5);
                if (recommendations.contains(actualFruit)) {
                    correct++;
                }
            }
        }
        return samples.size() > 0 ? (double) correct / samples.size() : 0.0;
    }

    /**
     * 保存训练好的模型到文件系统
     */
    private void saveModel(TrainingTask task, Object classifier) {
        try {
            // 创建模型目录
            Path modelDir = Paths.get("models", "task_" + task.getId());
            Files.createDirectories(modelDir);
            
            // 保存模型信息（简化版本）
            Path modelInfoPath = modelDir.resolve("model_info.txt");
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(modelInfoPath))) {
                writer.println("Model Type: " + task.getModelType());
                writer.println("Task Name: " + task.getName());
                writer.println("Final Accuracy: " + task.getCurrentAccuracy());
                writer.println("Final Loss: " + task.getCurrentLoss());
                writer.println("Training Time: " + (System.currentTimeMillis() - task.getStartTime()) + "ms");
                writer.println("Vocabulary Size: " + vocabulary.size());
                
                // 根据分类器类型保存额外信息
                if (classifier instanceof FruitRecommendationClassifier) {
                    FruitRecommendationClassifier fruitClassifier = (FruitRecommendationClassifier) classifier;
                    writer.println("Classifier Type: FruitRecommendationClassifier");
                    writer.println("Fruit Scores Count: " + fruitClassifier.fruitScores.size());
                    writer.println("User Preferences Count: " + fruitClassifier.userPreferences.size());
                    
                    // 保存水果推荐模型数据
                    Path fruitDataPath = modelDir.resolve("fruit_data.ser");
                    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(fruitDataPath))) {
                        oos.writeObject(fruitClassifier.fruitScores);
                        oos.writeObject(fruitClassifier.userPreferences);
                        oos.writeObject(fruitClassifier.fruitFeatures);
                    }
                } else if (classifier instanceof NaiveBayesClassifier) {
                    NaiveBayesClassifier nbClassifier = (NaiveBayesClassifier) classifier;
                    writer.println("Classifier Type: NaiveBayesClassifier");
                    writer.println("Class Probabilities Count: " + nbClassifier.classProbabilities.size());
                    
                    // 保存朴素贝叶斯模型数据
                    Path nbDataPath = modelDir.resolve("nb_data.ser");
                    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(nbDataPath))) {
                        oos.writeObject(nbClassifier.classProbabilities);
                        oos.writeObject(nbClassifier.featureProbabilities);
                        oos.writeObject(nbClassifier.vocabulary);
                    }
                }
            }
            
            // 保存词汇表
            Path vocabPath = modelDir.resolve("vocabulary.ser");
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(vocabPath))) {
                oos.writeObject(vocabulary);
            }
            
            task.addLog("INFO", "模型已保存到: " + modelDir.toString());
            
        } catch (IOException e) {
            task.addLog("ERROR", "保存模型失败: " + e.getMessage());
            log.error("Failed to save model for task {}", task.getId(), e);
        }
    }
    
    /**
     * 从文件系统加载训练好的模型
     */
    public Object loadModel(Long taskId) {
        try {
            Path modelDir = Paths.get("models", "task_" + taskId);
            if (!Files.exists(modelDir)) {
                throw new FileNotFoundException("模型目录不存在: " + modelDir.toString());
            }
            
            // 读取模型信息
            Path modelInfoPath = modelDir.resolve("model_info.txt");
            List<String> lines = Files.readAllLines(modelInfoPath);
            
            String modelType = null;
            for (String line : lines) {
                if (line.startsWith("Model Type: ")) {
                    modelType = line.substring("Model Type: ".length()).trim();
                    break;
                }
            }
            
            if (modelType == null) {
                throw new RuntimeException("无法确定模型类型");
            }
            
            // 根据模型类型加载相应的模型数据
            if ("fruit_recommendation".equals(modelType)) {
                Path fruitDataPath = modelDir.resolve("fruit_data.ser");
                if (!Files.exists(fruitDataPath)) {
                    throw new FileNotFoundException("水果推荐模型数据文件不存在: " + fruitDataPath.toString());
                }
                
                FruitRecommendationClassifier classifier = new FruitRecommendationClassifier();
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(fruitDataPath))) {
                    classifier.fruitScores = (Map<String, Double>) ois.readObject();
                    classifier.userPreferences = (Map<String, Map<String, Double>>) ois.readObject();
                    classifier.fruitFeatures = (Map<String, Map<String, Double>>) ois.readObject();
                }
                
                return classifier;
            } else if ("text_classification".equals(modelType)) {
                Path nbDataPath = modelDir.resolve("nb_data.ser");
                if (!Files.exists(nbDataPath)) {
                    throw new FileNotFoundException("朴素贝叶斯模型数据文件不存在: " + nbDataPath.toString());
                }
                
                NaiveBayesClassifier classifier = new NaiveBayesClassifier();
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(nbDataPath))) {
                    classifier.classProbabilities = (Map<String, Double>) ois.readObject();
                    classifier.featureProbabilities = (Map<String, Map<String, Double>>) ois.readObject();
                    classifier.vocabulary = (Set<String>) ois.readObject();
                }
                
                return classifier;
            }
            
            throw new RuntimeException("不支持的模型类型: " + modelType);
            
        } catch (Exception e) {
            log.error("加载模型失败，任务ID: {}", taskId, e);
            throw new RuntimeException("加载模型失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 使用模型进行预测
     */
    public Map<String, Object> predictWithModel(Long taskId, Map<String, Object> input) {
        TrainingTask task = runningTasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("训练任务不存在");
        }
        
        if (!"completed".equals(task.getStatus())) {
            throw new RuntimeException("模型尚未训练完成");
        }
        
        // 加载模型
        Object model = loadModel(taskId);
        if (model == null) {
            throw new RuntimeException("模型加载失败");
        }
        
        // 根据模型类型进行预测
        Map<String, Object> result = new HashMap<>();
        String text = (String) input.get("text");
        
        if (model instanceof FruitRecommendationClassifier) {
            // 水果推荐预测
            FruitRecommendationClassifier classifier = (FruitRecommendationClassifier) model;
            List<String> recommendations = classifier.recommend(text, 5); // 推荐前5个水果
            
            result.put("type", "fruit_recommendation");
            result.put("recommendations", recommendations);
            result.put("input", text);
            
        } else if (model instanceof NaiveBayesClassifier) {
            // 文本分类预测
            NaiveBayesClassifier classifier = (NaiveBayesClassifier) model;
            Map<String, Double> scores = classifier.predict(tokenize(text));
            String predictedClass = classifier.classify(tokenize(text));
            
            result.put("type", "text_classification");
            result.put("predictedClass", predictedClass);
            result.put("scores", scores);
            result.put("input", text);
            
        } else {
            throw new RuntimeException("不支持的模型类型");
        }
        
        return result;
    }
    
    /**
     * 检查模型是否存在
     */
    public boolean isModelExists(Long taskId) {
        Path modelDir = Paths.get("models", "task_" + taskId);
        Path modelInfoPath = modelDir.resolve("model_info.txt");
        return Files.exists(modelInfoPath);
    }
    
    /**
     * 停止训练任务
     */
    public boolean stopTraining(Long taskId) {
        TrainingTask task = runningTasks.get(taskId);
        if (task != null) {
            task.setStatus("stopped");
            if (task.getFuture() != null) {
                task.getFuture().cancel(true);
            }
            task.addLog("INFO", "训练任务已停止");
            return true;
        }
        return false;
    }

    /**
     * 评估模型
     */
    public Map<String, Object> evaluateModel(Long taskId, Dataset testDataset) {
        TrainingTask task = runningTasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("训练任务不存在");
        }
        
        if (!"completed".equals(task.getStatus())) {
            throw new RuntimeException("模型尚未训练完成");
        }
        
        Map<String, Object> evaluation = new HashMap<>();
        
        // 预处理测试数据
        preprocessDataset(testDataset);
        
        // 创建分类器进行评估
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(testDataset);
        
        // 在测试集上评估模型
        int correct = 0;
        int total = testDataset.getSamples().size();
        
        Map<String, Integer> confusionMatrix = new HashMap<>();
        
        for (TextSample sample : testDataset.getSamples()) {
            String predicted = classifier.classify(sample.getTokens());
            String actual = sample.getLabel();
            
            if (predicted.equals(actual)) {
                correct++;
            }
            
            // 更新混淆矩阵
            String key = actual + "_" + predicted;
            confusionMatrix.put(key, confusionMatrix.getOrDefault(key, 0) + 1);
        }
        
        double accuracy = (double) correct / total;
        
        evaluation.put("accuracy", accuracy);
        evaluation.put("totalSamples", total);
        evaluation.put("correctPredictions", correct);
        evaluation.put("confusionMatrix", confusionMatrix);
        evaluation.put("evaluationTime", new Date());
        
        // 更新任务状态
        task.addLog("INFO", String.format("模型评估完成，准确率: %.2f%%", accuracy * 100));
        
        return evaluation;
    }
    
    /**
     * 部署模型
     */
    public Map<String, Object> deployModel(Long taskId, String deploymentName, String deploymentType) {
        TrainingTask task = runningTasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("训练任务不存在");
        }
        
        if (!"completed".equals(task.getStatus())) {
            throw new RuntimeException("模型尚未训练完成");
        }
        
        Map<String, Object> deployment = new HashMap<>();
        
        // 生成部署ID
        String deploymentId = "deploy_" + System.currentTimeMillis();
        
        // 模拟部署过程
        try {
            Thread.sleep(2000); // 模拟部署时间
            
            deployment.put("deploymentId", deploymentId);
            deployment.put("deploymentName", deploymentName);
            deployment.put("deploymentType", deploymentType);
            deployment.put("status", "deployed");
            deployment.put("endpoint", "http://localhost:8080/api/model/predict/" + deploymentId);
            deployment.put("deploymentTime", new Date());
            deployment.put("modelVersion", "v1.0");
            
            // 更新任务状态
            task.setStatus("deployed");
            task.addLog("INFO", String.format("模型已部署: %s (ID: %s)", deploymentName, deploymentId));
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("部署过程被中断", e);
        }
        
        return deployment;
     }
     
     /**
      * 导出模型
      */
     public void exportModel(Long taskId, String format) {
        TrainingTask task = runningTasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("训练任务不存在");
        }
        
        if (!"completed".equals(task.getStatus())) {
            throw new RuntimeException("模型尚未训练完成");
        }
        
        // 检查模型是否存在
        if (!isModelExists(taskId)) {
            throw new RuntimeException("模型文件不存在");
        }
        
        // 这里可以添加实际的导出逻辑
        task.addLog("INFO", "模型导出请求: format=" + format);
    }
 
     /**
      * 获取训练任务
      */
    public TrainingTask getTrainingTask(Long taskId) {
        return runningTasks.get(taskId);
    }

    /**
     * 获取所有训练任务
     */
    public Collection<TrainingTask> getAllTrainingTasks() {
        return runningTasks.values();
    }

    /**
     * 训练水果推荐模型
     */
    private void trainFruitRecommendationModel(TrainingTask task, String datasetPath) {
        try {
            // 加载水果推荐数据集
            Dataset dataset = loadDatasetFromFile(datasetPath, "fruit_recommendation");
            
            // 创建水果推荐分类器
            FruitRecommendationClassifier classifier = new FruitRecommendationClassifier();
            
            // 训练模型
            classifier.train(dataset);
            
            // 保存模型
            saveModel(task, classifier);
            
            task.addLog("INFO", "水果推荐模型训练完成");
            
        } catch (Exception e) {
            task.addLog("ERROR", "水果推荐模型训练失败: " + e.getMessage());
            throw new RuntimeException("训练失败", e);
        }
    }
    
    /**
     * 加载数据集
     */
    public Dataset loadDatasetFromFile(String filePath, String dataType) throws IOException {
        Dataset dataset = new Dataset();
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new FileNotFoundException("数据集文件不存在: " + filePath);
        }
        
        List<String> lines = Files.readAllLines(path);
        
        // 根据数据类型解析文件
        if ("text_classification".equals(dataType)) {
            // 假设CSV格式: text,label
            for (int i = 1; i < lines.size(); i++) { // 跳过标题行
                String line = lines.get(i);
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    dataset.addSample(parts[0].trim(), parts[1].trim());
                }
            }
        } else if ("fruit_recommendation".equals(dataType)) {
            // 水果推荐数据集格式: userId,age,gender,season,weather,history,fruitName,sweetness,acidity,texture,color,size,price,nutritionScore,recommendationScore
            for (int i = 1; i < lines.size(); i++) { // 跳过标题行
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length >= 15) {
                    // 将整行作为文本，水果名称作为标签
                    String text = line;
                    String label = parts[6].trim(); // 水果名称
                    dataset.addSample(text, label);
                }
            }
        }
        
        return dataset;
    }

    /**
     * 创建示例数据集
     */
    public Dataset createSampleDataset() {
        Dataset dataset = new Dataset();
        
        // 添加一些示例数据
        dataset.addSample("这个商品质量很好，非常满意", "positive");
        dataset.addSample("产品不错，值得推荐", "positive");
        dataset.addSample("很棒的购物体验", "positive");
        dataset.addSample("质量一般，不太满意", "negative");
        dataset.addSample("产品有问题，要求退货", "negative");
        dataset.addSample("服务态度差", "negative");
        dataset.addSample("还可以吧，一般般", "neutral");
        dataset.addSample("普通的产品", "neutral");
        
        return dataset;
    }

    /**
     * 关闭训练引擎
     */
    public void shutdown() {
        trainingExecutor.shutdown();
        try {
            if (!trainingExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                trainingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            trainingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 添加任务到引擎中
     * @param task 训练任务
     */
    public void addTask(TrainingTask task) {
        runningTasks.put(task.getId(), task);
    }
    
    /**
     * 获取所有任务
     * @return 所有任务的集合
     */
    public Map<Long, TrainingTask> getAllTasks() {
        return runningTasks;
    }
}