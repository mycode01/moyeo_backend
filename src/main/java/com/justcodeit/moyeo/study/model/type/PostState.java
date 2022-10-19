package com.justcodeit.moyeo.study.model.type;

public enum PostState {  // 상태 변경에 대한 정책을 회의록에서 찾을수가 없어서 대충 만듬
  ON_RECRUITING {
    @Override
    public PostState next() {
      return ON_PROGRESS;
    }
  }, ON_PROGRESS {
    @Override
    public PostState next() {
      return DELETED;
    }
  }, DELETED {
    @Override
    public PostState next() {
      throw new UnsupportedOperationException(); // delete 다음은 정해진바 없음
    }
  },
   // 이하 만약 지원한다면...
  ON_STOPPED_RECRUIT {
    @Override
    public PostState next() {
      throw new UnsupportedOperationException();// notYetImplementation
    }
  }, ON_PAUSED_PROGRESS {
    @Override
    public PostState next() {
      throw new UnsupportedOperationException(); // notYetImplementation
    }
  };


  abstract public PostState next();


}
