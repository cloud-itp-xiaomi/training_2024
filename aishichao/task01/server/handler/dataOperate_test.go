package handler

import (
	"net/http"
	"testing"
)

func TestConfirmTs(t *testing.T) {
	type args struct {
		queryParams map[string]string
		w           http.ResponseWriter
	}
	tests := []struct {
		name  string
		args  args
		want  int64
		want1 int64
		want2 string
		want3 string
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				queryParams: map[string]string{
					"start_ts": "1718170000",
					"end_ts":   "1718170000",
					"metric":   "mem.used.percent",
					"endpoint": "mcp",
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, got1, got2, got3 := ConfirmTs(tt.args.queryParams, tt.args.w)
			if got != tt.want {
				t.Errorf("ConfirmTs() got = %v, want %v", got, tt.want)
			}
			if got1 != tt.want1 {
				t.Errorf("ConfirmTs() got1 = %v, want %v", got1, tt.want1)
			}
			if got2 != tt.want2 {
				t.Errorf("ConfirmTs() got2 = %v, want %v", got2, tt.want2)
			}
			if got3 != tt.want3 {
				t.Errorf("ConfirmTs() got3 = %v, want %v", got3, tt.want3)
			}
		})
	}
}
