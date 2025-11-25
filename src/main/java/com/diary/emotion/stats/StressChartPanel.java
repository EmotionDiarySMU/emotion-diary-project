package com.diary.emotion.stats;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import com.diary.emotion.ui.UIColors;

/**
 * 스트레스 차트 패널
 * 스트레스 지수를 선 그래프로 표시
 */
public class StressChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final Font AXIS_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);

    private ChartPanel chartPanel;
    private JFreeChart lineChart;
    private String currentMode;
    private JLabel avgStressLabel;

    public StressChartPanel(String mode) {
        this.currentMode = mode;
        setLayout(new BorderLayout());
        setBackground(UIColors.BG_STATS);
        initChart();
        initAvgStressLabel();
    }

    private void initAvgStressLabel() {
        avgStressLabel = new JLabel(getAvgStressText(), SwingConstants.CENTER);
        avgStressLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // 16 -> 12로 변경
        avgStressLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10)); // 하단 패딩 10 -> 15로 증가
        add(avgStressLabel, BorderLayout.SOUTH);
    }

    private String getAvgStressText() {
        return "<html><center>평균 스트레스 지수<b>:</b> <b>0.0</b></center></html>";
    }

    public void setAvgStress(double avgStress) {
        avgStressLabel.setText(String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress));
    }

    public JLabel getAvgStressLabel() {
        return avgStressLabel;
    }

    private void initChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String xAxisLabel = getXAxisLabel(currentMode);

        lineChart = ChartFactory.createLineChart(
            null,
            xAxisLabel,
            "(%)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );

        styleChart();

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(UIColors.BG_STATS);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void styleChart() {
        lineChart.setBackgroundPaint(UIColors.BG_STATS);
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0);
        yAxis.setTickUnit(new NumberTickUnit(10.0));
        yAxis.setLabelAngle(Math.PI / 2.0);
        yAxis.setLabelFont(AXIS_LABEL_FONT);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3.5, -3.5, 7.0, 7.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setSeriesOutlinePaint(0, renderer.getSeriesPaint(0));
        renderer.setUseFillPaint(true);
        renderer.setUseOutlinePaint(true);

        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
    }

    /**
     * 스트레스 데이터 업데이트
     */
    public void updateData(DefaultCategoryDataset dataset, String mode) {
        this.currentMode = mode;
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setDataset(dataset);
        plot.getDomainAxis().setLabel(getXAxisLabel(mode));
    }

    private String getXAxisLabel(String mode) {
        if (mode == null) return "(요일)";

        switch (mode) {
            case "주간":
                return "(요일)";
            case "월간":
                return "(주)";
            case "연간":
                return "(월)";
            default:
                return "(요일)";
        }
    }
}

