package com.diary.emotion.stats;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import com.diary.emotion.ui.UIColors;

import java.util.Map;

/**
 * 감정 차트 패널
 * 감정별 횟수와 수치를 막대 그래프로 표시
 */
public class EmotionChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final Font AXIS_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);
    private static final String[] OFFICIAL_EMOTIONS = {
        "😊", "😆", "😍", "😌", "😂", "🤗",
        "😢", "😠", "😰", "😅", "😧", "😔"
    };

    private ChartPanel chartPanel;
    private JFreeChart barChart;

    public EmotionChartPanel() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BG_STATS);
        initChart();
    }

    private void initChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 12개 이모지를 모두 0으로 초기화
        for (String emotion : OFFICIAL_EMOTIONS) {
            dataset.setValue(0.0, "횟수(%)", emotion);
            dataset.setValue(0.0, "수치(%)", emotion);
        }

        barChart = ChartFactory.createBarChart(
            null,
            "(감정)",
            "(%)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );

        styleChart();

        chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(UIColors.BG_STATS);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void styleChart() {
        barChart.setBackgroundPaint(UIColors.BG_STATS);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0);
        yAxis.setTickUnit(new NumberTickUnit(10.0));
        yAxis.setLabelAngle(Math.PI / 2.0);
        yAxis.setLabelFont(AXIS_LABEL_FONT);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, new Color(100, 150, 255));
        renderer.setSeriesPaint(1, new Color(190, 220, 255));
        renderer.setItemMargin(0.1);

        plot.getDomainAxis().setCategoryMargin(0.3);
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
    }

    /**
     * 감정 데이터 업데이트
     */
    public void updateData(Map<String, Map<String, Double>> emotionData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> counts = emotionData.get("횟수");
        Map<String, Double> values = emotionData.get("수치");

        double totalCounts = (counts != null) ? counts.values().stream().mapToDouble(Double::doubleValue).sum() : 0.0;
        double totalValues = (values != null) ? values.values().stream().mapToDouble(Double::doubleValue).sum() : 0.0;

        for (String emotion : OFFICIAL_EMOTIONS) {
            double countValue = (counts != null) ? counts.getOrDefault(emotion, 0.0) : 0.0;
            double valueValue = (values != null) ? values.getOrDefault(emotion, 0.0) : 0.0;

            double countPercentage = (totalCounts > 0) ? (countValue / totalCounts) * 100 : 0.0;
            double valuePercentage = (totalValues > 0) ? (valueValue / totalValues) * 100 : 0.0;

            dataset.setValue(countPercentage, "횟수(%)", emotion);
            dataset.setValue(valuePercentage, "수치(%)", emotion);
        }

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setDataset(dataset);
    }


    /**
     * 범례 패널 생성
     */
    public static JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setOpaque(true);
        legendPanel.add(createLegendItem(new Color(100, 150, 255), "횟수(%)"));
        legendPanel.add(Box.createHorizontalStrut(5));
        legendPanel.add(createLegendItem(new Color(190, 220, 255), "수치"));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return legendPanel;
    }

    private static JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setOpaque(true);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(10, 10));
        colorBox.setBackground(color);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        itemPanel.add(colorBox);
        itemPanel.add(textLabel);
        return itemPanel;
    }
}

