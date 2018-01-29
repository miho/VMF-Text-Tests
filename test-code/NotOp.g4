grammar NotOp;


program: r1=rule1 | r2=rule2;

rule1: (content=~ABC 'altOne' | content=~DEF 'altTwo');

rule2: (content=~ABC 'altOne' | content=ABC 'altOne');

ABC: [abc]+;

DEF: [def]+;

STR: [a-z|0-9]+;

WS
    : [ \t\r\n]+ -> channel(HIDDEN)
;