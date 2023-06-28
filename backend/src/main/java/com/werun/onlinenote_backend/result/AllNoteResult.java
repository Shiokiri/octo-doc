package com.werun.onlinenote_backend.result;

import com.werun.onlinenote_backend.bean.CategoryBean;
import com.werun.onlinenote_backend.bean.NoteBean;
import com.werun.onlinenote_backend.bean.UserBean;
import lombok.*;

import java.util.List;

/**
 * @ClassName AllNoteResult
 * @Description AllNote类结果封装
 * @Create 2022-04-16
 * @Update
 **/
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllNoteResult {
    private List<NoteBean> noteBeanList;

    private Boolean success;

    private String msg;

    public AllNoteResult(List<NoteBean> noteBeanList)
    {
        this.noteBeanList = noteBeanList;
        this.success = true;
        this.msg = "success";
    }

    public AllNoteResult(Boolean success, String msg)
    {
        this.success = success;
        this.msg = msg;
    }
}
