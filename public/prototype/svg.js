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
function createFanStroke(cx, cy, radius, startDegree, finishDegree, width, strokeColor, strokeWidth) {
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

  const NS = "http://www.w3.org/2000/svg";
  const path = document.createElementNS(NS, "path");
  path.setAttributeNS(null, "d", startLine + " " + arc + " " + finishLine);
  path.setAttributeNS(null, "fill", "none");
  path.setAttributeNS(null, "stroke", strokeColor);
  path.setAttributeNS(null, "stroke-width", strokeWidth);

  return path;
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
function createFanShape(cx, cy, radius, startDegree, finishDegree, width, fillColor) {
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

  const NS = "http://www.w3.org/2000/svg";
  const path = document.createElementNS(NS, "path");
  path.setAttributeNS(null, "d", startLine + " " + arc + " " + finishLine + " " + innerArc);
  path.setAttributeNS(null, "fill", fillColor);

  return path;
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
function createText(cx, cy, radius, startDegree, finishDegree, fontSize, fontColor, text) {
    //textを表示する角度(扇形の真ん中);
    var degree = (startDegree + finishDegree) / 2;
    //textの座標;
    var textX = cx - radius * Math.sin(degree / 180 * Math.PI);
    var textY = cy + radius * Math.cos(degree / 180 * Math.PI);

    //text要素を作成;
    var NS = 'http://www.w3.org/2000/svg';
    var svgText = document.createElementNS(NS, 'text');
    svgText.textContent = text;
    svgText.setAttributeNS(null, 'x', textX);
    svgText.setAttributeNS(null, 'y', textY);
    svgText.setAttributeNS(null, 'fill', fontColor);
    svgText.setAttributeNS(null, 'dominant-baseline', 'middle');
    svgText.setAttributeNS(null, 'font-size', fontSize);
    svgText.setAttributeNS(null, 'text-anchor', 'middle');

    return svgText;
}


// const strokePath = createFanStroke(100, 200, 100, 210, 330, 16, "#555555", 1)
// clutch.appendChild(strokePath);

// const shapePath = createFanShape(100, 200, 100, 210, 330, 16, "red")
// clutch.appendChild(shapePath);

// const text = createText(200, 200, 100, 210, 330, 40, "#111", "1111111111111111111");
// clutch.appendChild(text);

const cx = 300;
const cy = 100;
const radius = 100;

const maxRpm = 9400;
const rpm = 5000;
const engineDegree = 300;
const engineStartDegree = 30;
const separateDegree = 300 / 9400 * 1000;
const engineFontSize = 8;
const engineMeter = document.getElementById("engineMeter");
const engineBackgroundPath = createFanShape(cx, cy, radius, engineStartDegree, engineStartDegree + engineDegree, 8, "red");
engineMeter.appendChild(engineBackgroundPath);

Array.from({length: Math.floor(maxRpm / 1000) + 1}, (v, k) => k).map(i => {
  console.log(i);
  const text = createText(cx, cy, radius - engineFontSize * 2, engineStartDegree + separateDegree * (i - 0.5), engineStartDegree + separateDegree * (i + 0.5), engineFontSize, "#111", i.toString());
  engineMeter.appendChild(text);
});




const clutch = document.getElementById("clutch");
const path = createFanShape(100, 200, 100, 60, 120, 16, "#555555", 2);
clutch.appendChild(path);

// const clutchValue = document.getElementById("clutchValue");
// const path1 = cradiuseateFanShape(96, 200, 100, 34, 146, "none", "#DDDDDD", 12);
// clutchValue.appendChild(path1);
//



