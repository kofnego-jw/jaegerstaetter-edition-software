import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss'],
})
export class CommentComponent implements OnInit {
  showComment: boolean;

  constructor() {}

  ngOnInit(): void {
    this.showComment = false;
  }

  toggleShowComment(): void {
    this.showComment = !this.showComment;
  }
}
