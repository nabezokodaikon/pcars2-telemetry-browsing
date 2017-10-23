import React from "react";

/*
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * startDegree: 扇形の始まりの中心角(度数法)
 * finishDegree: 扇形の終わりの中心角(度数法)
 * width: 扇の幅
 * strokeColor: 線の色
 * strokeWidth: 線の幅
*/
export function createFanStroke(cx, cy, radius, startDegree, finishDegree, width, strokeColor, strokeWidth) {
  const startSinX = Math.sin(startDegree / 180 * Math.PI);
  const startCosY = Math.cos(startDegree / 180 * Math.PI);
  const finishSinX = Math.sin(finishDegree / 180 * Math.PI);
  const finishCosY = Math.cos(finishDegree / 180 * Math.PI);

  const innerRadius = radius - width;
  const innerStartX = cx - innerRadius * startSinX;
  const innerStartY = cy + innerRadius * startCosY;
  const innerFinishX = cx - innerRadius * finishSinX;
  const innerFinishY = cy + innerRadius * finishCosY;

  const arcStartX = cx - radius * startSinX;
  const arcStartY = cy + radius * startCosY;
  const arcFinishX = cx - radius * finishSinX;
  const arcFinishY = cy + radius * finishCosY;

  const largeArcFlag = (finishDegree - startDegree <= 180) ? 0 : 1;

  const startLine = "M" + innerStartX + " " + innerStartY + " L " + arcStartX + " " + arcStartY;
  const arc = "A" + radius + " " + radius + " " + 0 + " " + largeArcFlag + " " + 1 + " " + arcFinishX + " " + arcFinishY;
  const finishLine = "L" + innerFinishX + " " + innerFinishY + " ";

  return (
    <path
      d={startLine + " " + arc + " " + finishLine}
      fill="none"
      stroke={strokeColor}
      strokeWidth={strokeWidth}
    />
  );
}

/*
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * startDegree: 扇形の始まりの中心角(度数法)
 * finishDegree: 扇形の終わりの中心角(度数法)
 * width: 扇の幅
 * fillColor: 扇の色
*/
export function createFanShape(cx, cy, radius, startDegree, finishDegree, width, fillColor) {
  const startSinX = Math.sin(startDegree / 180 * Math.PI);
  const startCosY = Math.cos(startDegree / 180 * Math.PI);
  const finishSinX = Math.sin(finishDegree / 180 * Math.PI);
  const finishCosY = Math.cos(finishDegree / 180 * Math.PI);

  const innerRadius = radius - width;
  const innerStartX = cx - innerRadius * startSinX;
  const innerStartY = cy + innerRadius * startCosY;
  const innerFinishX = cx - innerRadius * finishSinX;
  const innerFinishY = cy + innerRadius * finishCosY;

  const arcStartX = cx - radius * startSinX;
  const arcStartY = cy + radius * startCosY;
  const arcFinishX = cx - radius * finishSinX;
  const arcFinishY = cy + radius * finishCosY;

  const largeArcFlag = (finishDegree - startDegree <= 180) ? 0 : 1;

  const startLine = "M" + innerStartX + " " + innerStartY + " L " + arcStartX + " " + arcStartY;
  const arc = "A" + radius + " " + radius + " " + 0 + " " + largeArcFlag + " " + 1 + " " + arcFinishX + " " + arcFinishY;
  const finishLine = "L" + innerFinishX + " " + innerFinishY + " ";
  const innerArc = "A" + innerRadius + " " + innerRadius + " " + 0 + " " + largeArcFlag + " " + 0 + " " + innerStartX + " " + innerStartY + " Z";

  return (
    <path
      d={startLine + " " + arc + " " + finishLine + " " + innerArc}
      fill={fillColor}
    />
  );
}

/*
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * startDegree: 扇形の始まりの中心角(度数法)
 * finishDegree: 扇形の終わりの中心角(度数法)
 * fontSize: 文字の大きさ
 * fontColor: 文字の色
 * text: 文字
*/
export function createFanText(cx, cy, radius, startDegree, finishDegree, fontSize, fontColor, text) {
  var degree = (startDegree + finishDegree) / 2;
  var textX = cx - radius * Math.sin(degree / 180 * Math.PI);
  var textY = cy + radius * Math.cos(degree / 180 * Math.PI);

  return (
    <text
      key={text}
      x={textX}
      y={textY}
      fill={fontColor}
      textAnchor="middle"
      dominantBaseline="middle"
      style={{fontSize: fontSize}}
    >
      {text}
    </text>
  );
}
