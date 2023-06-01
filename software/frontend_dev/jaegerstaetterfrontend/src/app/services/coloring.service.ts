import { Injectable } from '@angular/core';
import * as Color from 'color';
import { ReplaySubject, Subject } from 'rxjs';
import { ColoringInfo, SingleColoringInfo } from '../models/frontend';

@Injectable({
  providedIn: 'root',
})
export class ColoringService {
  static readonly MY_DEFAULT_COLOR: Color = Color('#19334b');

  static readonly MY_COLORS: Color[] = [
    Color(ColoringService.MY_DEFAULT_COLOR.toString()).rotate(60),
    Color(ColoringService.MY_DEFAULT_COLOR.toString()).rotate(120),
    Color(ColoringService.MY_DEFAULT_COLOR.toString()).rotate(180),
    Color(ColoringService.MY_DEFAULT_COLOR.toString()).rotate(240),
    Color(ColoringService.MY_DEFAULT_COLOR.toString()).rotate(300),
  ];

  defaultColor: string = ColoringService.MY_DEFAULT_COLOR.toString();
  coloringInfos: SingleColoringInfo[] = [];

  coloringInfo$: ReplaySubject<ColoringInfo> = new ReplaySubject(1);

  constructor() {
    this.coloringInfo$.next(
      new ColoringInfo(ColoringService.MY_DEFAULT_COLOR.toString(), [])
    );
  }

  setDefaultColor(defColor: Color | undefined): void {
    this.defaultColor = defColor ? defColor.toString() : this.defaultColor;
    this.publishColoringInfo();
  }

  resetColoringInfo(): void {
    this.coloringInfos = [];
    this.publishColoringInfo();
  }

  setHands(hands: string[]): void {
    this.resetColoringInfo();
    if (hands && hands.length) {
      hands.forEach((hand, i) => {
        const index =
          i >= ColoringService.MY_COLORS.length
            ? ColoringService.MY_COLORS.length - 1
            : i;
        const info = new SingleColoringInfo(
          hand,
          ColoringService.MY_COLORS[index].toString()
        );
        this.coloringInfos.push(info);
      });
      this.publishColoringInfo();
    }
  }

  publishColoringInfo(): void {
    const ci: ColoringInfo = new ColoringInfo(
      this.defaultColor,
      this.coloringInfos
    );
    this.coloringInfo$.next(ci);
  }
}
