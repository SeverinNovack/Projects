%facts
checkstart(yes).

fillY([[a1,empty], [b1,empty], [c1,empty],
[a2,empty], [b2,empty], [c2,empty],
[a3,empty], [b3,empty], [c3,empty]]).

%predicates
start:-
    fillY(Y), printfield(Y), mode(Y).

mode(Y):- write('Do you want to play vs the AI? (yes/no): '), read(X), checkstart(X) -> aistart(Y); play2(Y).

%playing against the ai

aistart(Y):-
    nl, write('Do you want to start (yes/no): '), read(X), begin(X,Y).

begin(X,Y):-
    (checkstart(X) -> input(Y); type(Y)).

%player input
input(Y):-
    nl, write('What field: (a1,a2,a3,b1,...)'), read(X), rinput(X,Y).
rinput(X,Y):-
    member([X,empty],Y) -> update(Y,X), type(Y); write('Invalid input, try again: '), nl, input(Y).

update(Y,X):-
    delete(Y,[X,empty],L1), append([[X,cross]],L1,L2), playertie(L2), not(playerwin(L2)) -> printfield(L2);
    delete(Y,[X,empty],L1), append([[X,cross]],L1,L2), type(L2).

%ai input
type(Y):-
    calcfield(Y),write(Y).
calcfield(Y):-
    member([b2,empty],Y) -> aiupdate(b2,Y); checkforaiwinable(Y),checkforplayerwinable(Y), calc(Y).

% corner and edge calculation, the numbers with the letter is the field
% you are looking from to win
calc(Y):-
    not(cornerc321(Y)), not(cornera321(Y)), not(cornera123(Y)), not(cornerc123(Y)),
    not(edgea2(Y)), not(edgeb1(Y)), not(edgeb3(Y)), not(edgec2(Y)).

cornerc321(Y):-
    (member([c3,empty],Y) -> aiupdate(c3,Y);
               ((member([c3,circle],Y) ->
               ((member([a1,empty],Y) -> aiupdate(a1,Y)))))).

cornera321(Y):-
    (member([a3,empty],Y) -> aiupdate(a3,Y);
               ((member([a3,circle],Y) ->
               (member([c1,empty],Y) -> aiupdate(c1,Y))))).

cornera123(Y):-
    (member([a1,empty],Y) -> aiupdate(a1,Y);
               ((member([a1,circle],Y) ->
               ((member([c3,empty],Y) -> aiupdate(c3,Y)))))).

cornerc123(Y):-
    (member([c1,empty],Y) -> aiupdate(c1,Y);
               ((member([c1,circle],Y) ->
               ((member([a3,empty],Y) -> aiupdate(ca3,Y)))))).

edgea2(Y):-
    member([a2,empty],Y) -> aiupdate(a2,Y).

edgeb1(Y):-
    member([b1,empty],Y) -> aiupdate(b1,Y).

edgeb3(Y):-
    member([b3,empty],Y) -> aiupdate(b3,Y).

edgec2(Y):-
    member([c2,empty],Y) -> aiupdate(c2,Y).

aiupdate(X,Y):-
    delete(Y,[X,empty], L1), append([[X,circle]], L1,L2), printfield(L2), aiwin(L2), aitie(L2).

% check whether the ai is able to win and if it is it ends the game,
% s=side, d=down, di=diagonal
checkforaiwinable(Y):-
    not(aicheckas12(Y)), not(aicheckas13(Y)), not(aicheckas23(Y)),
    not(aicheckbs12(Y)), not(aicheckbs13(Y)), not(aicheckbs23(Y)),
    not(aicheckcs12(Y)), not(aicheckcs13(Y)), not(aicheckcs23(Y)),
    not(aicheckadab(Y)), not(aicheckadac(Y)), not(aicheckadbc(Y)),
    not(aicheckbdab(Y)), not(aicheckbdac(Y)), not(aicheckbdbc(Y)),
    not(aicheckcdab(Y)), not(aicheckcdac(Y)), not(aicheckcdbc(Y)),
    not(aicheckadi12(Y)), not(aicheckadi13(Y)), not(aicheckadi23(Y)),
    not(aicheckcdi12(Y)), not(aicheckcdi13(Y)), not(aicheckcdi23(Y)).

aicheckas12(Y):-
    member([a3,empty],Y) -> member([a1,circle], Y) -> (member([a2,circle],Y) -> aiupdate(a3,Y)).
aicheckas13(Y):-
    member([a2,empty],Y) -> member([a1,circle], Y) -> (member([a3,circle],Y) -> aiupdate(a2,Y)).
aicheckas23(Y):-
    member([a1,empty],Y) -> member([a2,circle], Y) -> (member([a3,circle],Y) -> aiupdate(a1,Y)).
aicheckbs12(Y):-
    member([b3,empty],Y) -> member([b1,circle], Y) -> (member([b2,circle],Y) -> aiupdate(b3,Y)).
aicheckbs13(Y):-
    member([b2,empty],Y) -> member([b1,circle], Y) -> (member([b3,circle],Y) -> aiupdate(b2,Y)).
aicheckbs23(Y):-
    member([b1,empty],Y) -> member([b2,circle], Y) -> (member([b3,circle],Y) -> aiupdate(b1,Y)).
aicheckcs12(Y):-
    member([c3,empty],Y) -> member([c1,circle], Y) -> (member([c2,circle],Y) -> aiupdate(c3,Y)).
aicheckcs13(Y):-
    member([c2,empty],Y) -> member([c1,circle], Y) -> (member([c3,circle],Y) -> aiupdate(c2,Y)).
aicheckcs23(Y):-
    member([c1,empty],Y) -> member([c2,circle], Y) -> (member([c3,circle],Y) -> aiupdate(c1,Y)).

aicheckadab(Y):-
    member([c1,empty],Y) -> member([a1,circle], Y) -> (member([b1,circle],Y) -> aiupdate(c1,Y)).
aicheckadac(Y):-
    member([b1,empty],Y) -> member([a1,circle], Y) -> (member([c1,circle],Y) -> aiupdate(b1,Y)).
aicheckadbc(Y):-
    member([a1,empty],Y) -> member([b1,circle], Y) -> (member([c1,circle],Y) -> aiupdate(a1,Y)).
aicheckbdab(Y):-
    member([c2,empty],Y) -> member([a2,circle], Y) -> (member([b2,circle],Y) -> aiupdate(c2,Y)).
aicheckbdac(Y):-
    member([b2,empty],Y) -> member([a2,circle], Y) -> (member([c2,circle],Y) -> aiupdate(b2,Y)).
aicheckbdbc(Y):-
    member([a2,empty],Y) -> member([b2,circle], Y) -> (member([c2,circle],Y) -> aiupdate(a2,Y)).
aicheckcdab(Y):-
    member([c3,empty],Y) -> member([a3,circle], Y) -> (member([b3,circle],Y) -> aiupdate(c3,Y)).
aicheckcdac(Y):-
    member([b3,empty],Y) -> member([a3,circle], Y) -> (member([c3,circle],Y) -> aiupdate(b3,Y)).
aicheckcdbc(Y):-
    member([a3,empty],Y) -> member([b3,circle], Y) -> (member([c3,circle],Y) -> aiupdate(a3,Y)).

aicheckadi12(Y):-
    member([c3,empty],Y) -> member([a1,circle], Y) -> (member([b2,circle],Y) -> aiupdate(c3,Y)).
aicheckadi13(Y):-
    member([b2,empty],Y) -> member([a1,circle], Y) -> (member([c3,circle],Y) -> aiupdate(b2,Y)).
aicheckadi23(Y):-
    member([a1,empty],Y) -> member([b2,circle], Y) -> (member([c3,circle],Y) -> aiupdate(a1,Y)).
aicheckcdi12(Y):-
    member([a3,empty],Y) -> member([c1,circle], Y) -> (member([b2,circle],Y) -> aiupdate(a3,Y)).
aicheckcdi13(Y):-
    member([b2,empty],Y) -> member([c1,circle], Y) -> (member([a3,circle],Y) -> aiupdate(b2,Y)).
aicheckcdi23(Y):-
    member([c1,empty],Y) -> member([b2,circle], Y) -> (member([a3,circle],Y) -> aiupdate(c1,Y)).


%check if the player is able to win and if he is stop him
checkforplayerwinable(Y):-
    not(checkas12(Y)), not(checkas13(Y)), not(checkas23(Y)),
    not(checkbs12(Y)), not(checkbs13(Y)), not(checkbs23(Y)),
    not(checkcs12(Y)), not(checkcs13(Y)), not(checkcs23(Y)),
    not(checkadab(Y)), not(checkadac(Y)), not(checkadbc(Y)),
    not(checkbdab(Y)), not(checkbdac(Y)), not(checkbdbc(Y)),
    not(checkcdab(Y)), not(checkcdac(Y)), not(checkcdbc(Y)),
    not(checkadi12(Y)), not(checkadi13(Y)), not(checkadi23(Y)),
    not(checkcdi12(Y)), not(checkcdi13(Y)), not(checkcdi23(Y)),
    not(checkc2a1corner(Y)), not(checka2a1corner(Y)), not(checkb1a1corner(Y)), not(checkb3a1corner(Y)),
    not(checkc2a3corner(Y)), not(checka2a3corner(Y)), not(checkb1a3corner(Y)), not(checkb3a3corner(Y)).

checkas12(Y):-
    member([a3,empty],Y) -> member([a1,cross], Y) -> (member([a2,cross],Y) -> aiupdate(a3,Y)).
checkas13(Y):-
    member([a2,empty],Y) -> member([a1,cross], Y) -> (member([a3,cross],Y) -> aiupdate(a2,Y)).
checkas23(Y):-
    member([a1,empty],Y) -> member([a2,cross], Y) -> (member([a3,cross],Y) -> aiupdate(a1,Y)).
checkbs12(Y):-
    member([b3,empty],Y) -> member([b1,cross], Y) -> (member([b2,cross],Y) -> aiupdate(b3,Y)).
checkbs13(Y):-
    member([b2,empty],Y) -> member([b1,cross], Y) -> (member([b3,cross],Y) -> aiupdate(b2,Y)).
checkbs23(Y):-
    member([b1,empty],Y) -> member([b2,cross], Y) -> (member([b3,cross],Y) -> aiupdate(b1,Y)).
checkcs12(Y):-
    member([c3,empty],Y) -> member([c1,cross], Y) -> (member([c2,cross],Y) -> aiupdate(c3,Y)).
checkcs13(Y):-
    member([c2,empty],Y) -> member([c1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(c2,Y)).
checkcs23(Y):-
    member([c1,empty],Y) -> member([c2,cross], Y) -> (member([c3,cross],Y) -> aiupdate(c1,Y)).

checkadab(Y):-
    member([c1,empty],Y) -> member([a1,cross], Y) -> (member([b1,cross],Y) -> aiupdate(c1,Y)).
checkadac(Y):-
    member([b1,empty],Y) -> member([a1,cross], Y) -> (member([c1,cross],Y) -> aiupdate(b1,Y)).
checkadbc(Y):-
    member([a1,empty],Y) -> member([b1,cross], Y) -> (member([c1,cross],Y) -> aiupdate(a1,Y)).
checkbdab(Y):-
    member([c2,empty],Y) -> member([a2,cross], Y) -> (member([b2,cross],Y) -> aiupdate(c2,Y)).
checkbdac(Y):-
    member([b2,empty],Y) -> member([a2,cross], Y) -> (member([c2,cross],Y) -> aiupdate(b2,Y)).
checkbdbc(Y):-
    member([a2,empty],Y) -> member([b2,cross], Y) -> (member([c2,cross],Y) -> aiupdate(a2,Y)).
checkcdab(Y):-
    member([c3,empty],Y) -> member([a3,cross], Y) -> (member([b3,cross],Y) -> aiupdate(c3,Y)).
checkcdac(Y):-
    member([b3,empty],Y) -> member([a3,cross], Y) -> (member([c3,cross],Y) -> aiupdate(b3,Y)).
checkcdbc(Y):-
    member([a3,empty],Y) -> member([b3,cross], Y) -> (member([c3,cross],Y) -> aiupdate(a3,Y)).

checkadi12(Y):-
    member([c3,empty],Y) -> member([a1,cross], Y) -> (member([b2,cross],Y) -> aiupdate(c3,Y)).
checkadi13(Y):-
    member([b2,empty],Y) -> member([a1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(b2,Y)).
checkadi23(Y):-
    member([a1,empty],Y) -> member([b2,cross], Y) -> (member([c3,cross],Y) -> aiupdate(a1,Y)).
checkcdi12(Y):-
    member([a3,empty],Y) -> member([c1,cross], Y) -> (member([b2,cross],Y) -> aiupdate(a3,Y)).
checkcdi13(Y):-
    member([b2,empty],Y) -> member([c1,cross], Y) -> (member([a3,cross],Y) -> aiupdate(b2,Y)).
checkcdi23(Y):-
    member([c1,empty],Y) -> member([b2,cross], Y) -> (member([a3,cross],Y) -> aiupdate(c1,Y)).

checkc2a3corner(Y):-
    member([c2,empty],Y) -> member([a3,cross], Y) -> (member([c1,cross],Y) -> aiupdate(c2,Y)).
checka2a3corner(Y):-
    member([a2,empty],Y) -> member([a3,cross], Y) -> (member([c1,cross],Y) -> aiupdate(a2,Y)).
checkb1a3corner(Y):-
    member([b1,empty],Y) -> member([a3,cross], Y) -> (member([c1,cross],Y) -> aiupdate(b1,Y)).
checkb3a3corner(Y):-
    member([b3,empty],Y) -> member([a3,cross], Y) -> (member([c1,cross],Y) -> aiupdate(b3,Y)).

checkc2a1corner(Y):-
    member([c2,empty],Y) -> member([a1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(c2,Y)).
checka2a1corner(Y):-
    member([a2,empty],Y) -> member([a1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(a2,Y)).
checkb1a1corner(Y):-
    member([b1,empty],Y) -> member([a1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(b1,Y)).
checkb3a1corner(Y):-
    member([b3,empty],Y) -> member([a1,cross], Y) -> (member([c3,cross],Y) -> aiupdate(b3,Y)).





%print field
printfield(L2):-
    write('_____________'), nl, write('|       |       |       |'), nl,
    aprint(L2), nl, write('|____|____|____|'), nl, write('|       |       |       |'), nl,
    bprint(L2), nl, write('|____|____|____|'), nl, write('|       |       |       |'), nl,
    cprint(L2), nl, write('|____|____|____|'), nl, write('    1      2     3'), nl.

aprint(X):-
    write('|   '), (member([a1,empty],X) -> write('  '); (member([a1, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([a2,empty],X) -> write('  '); (member([a2, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([a3,empty],X) -> write('  '); (member([a3, cross],X) -> write('X'); write('0'))), write('  |  a').

bprint(X):-
    write('|   '), (member([b1,empty],X) -> write('  '); (member([b1, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([b2,empty],X) -> write('  '); (member([b2, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([b3,empty],X) -> write('  '); (member([b3, cross],X) -> write('X'); write('0'))), write('  |  b').

cprint(X):-
    write('|   '), (member([c1,empty],X) -> write('  '); (member([c1, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([c2,empty],X) -> write('  '); (member([c2, cross],X) -> write('X'); write('0'))), write('  |   '),
    (member([c3,empty],X) -> write('  '); (member([c3, cross],X) -> write('X'); write('0'))), write('  |  c').


%check for win/tie
%check for player
playerwin(Y):-
    not(checkas(Y)),not(checkbs(Y)), not(checkcs(Y)),
    not(checkad(Y)),not(checkbd(Y)),not(checkcd(Y)),
    not(checkadi(Y)),not(checkcdi(Y)).

checkas(Y):-
    member([a1,cross],Y) -> member([a2,cross],Y) -> member([a3,cross],Y) -> nl, playerwinmsg, end.
checkbs(Y):-
    member([b1,cross],Y) -> member([b2,cross],Y) -> member([b3,cross],Y) -> nl, playerwinmsg, end.
checkcs(Y):-
    member([c1,cross],Y) -> member([c2,cross],Y) -> member([c3,cross],Y) -> nl, playerwinmsg, end.
checkad(Y):-
    member([a1,cross],Y) -> member([b1,cross],Y) -> member([c1,cross],Y) -> nl, playerwinmsg, end.
checkbd(Y):-
    member([a2,cross],Y) -> member([b2,cross],Y) -> member([c2,cross],Y) -> nl, playerwinmsg, end.
checkcd(Y):-
    member([a3,cross],Y) -> member([b3,cross],Y) -> member([c3,cross],Y) -> nl, playerwinmsg, end.
checkadi(Y):-
    member([a1,cross],Y) -> member([b2,cross],Y) -> member([c3,cross],Y) -> nl, playerwinmsg, end.
checkcdi(Y):-
    member([a3,cross],Y) -> member([b2,cross],Y) -> member([c1,cross],Y) -> nl, playerwinmsg, end.

playerwinmsg:-
    write('Congratulations, you have won!').


%check for ai
aiwin(Y):-
    not(aicheckas(Y)), not(aicheckbs(Y)), not(aicheckcs(Y)),
    not(aicheckad(Y)), not(aicheckbd(Y)), not(aicheckcd(Y)),
    not(aicheckadi(Y)), not(aicheckcdi(Y)).


aicheckas(Y):-
    member([a1,circle],Y) -> member([a2,circle],Y) -> member([a3,circle],Y) -> nl, aiwinmsg, end.
aicheckbs(Y):-
    member([b1,circle],Y) -> member([b2,circle],Y) -> member([b3,circle],Y) -> nl, aiwinmsg, end.
aicheckcs(Y):-
    member([c1,circle],Y) -> member([c2,circle],Y) -> member([c3,circle],Y) -> nl, aiwinmsg, end.
aicheckad(Y):-
    member([a1,circle],Y) -> member([b1,circle],Y) -> member([c1,circle],Y) -> nl, aiwinmsg, end.
aicheckbd(Y):-
    member([a2,circle],Y) -> member([b2,circle],Y) -> member([c2,circle],Y) -> nl, aiwinmsg, end.
aicheckcd(Y):-
    member([a3,circle],Y) -> member([b3,circle],Y) -> member([c3,circle],Y) -> nl, aiwinmsg, end.
aicheckadi(Y):-
    member([a1,circle],Y) -> member([b2,circle],Y) -> member([c3,circle],Y) -> nl, aiwinmsg, end.
aicheckcdi(Y):-
    member([a3,circle],Y) -> member([b2,circle],Y) -> member([c1,circle],Y) -> nl, aiwinmsg, end.

aiwinmsg:-
    write('The AI wins!').

%check for tie
playertie(Y):-
    member([_,empty],Y)-> type(Y); printfield(Y), nl, tiemsg, end.
aitie(Y):-
    member([_,empty],Y)-> input(Y); printfield(Y), nl, tiemsg, end.

tiemsg:-
    write('A tie has been reached').

%end of the game and asking for another round
end:-
    nl, write('Do you want to play again? (yes/no): '), read(X), checkstart(X) -> start; halt.


%2PlayerMode
play2(Y):-
    inputP1(Y).

%turn for player1
inputP1(Y):-
    nl, write('Player1: What field: (a1,a2,a3,b1,...)'), read(X), rinputP1(X,Y).

rinputP1(X,Y):-
    member([X,empty],Y) -> updatep1(Y,X), inputP2(Y); write('Invalid input, try again: '), nl, inputP1(Y).

updatep1(Y,X):-
    delete(Y,[X,empty],L1), append([[X,cross]],L1,L2), printfield(L2), player1win(L2), player1tie(L2);
    delete(Y,[X,empty],L1), append([[X,cross]],L1,L2), inputP2(L2).

%turn for player2
inputP2(Y):-
    nl, write('Player2: What field: (a1,a2,a3,b1,...'), read(X), rinputP2(X,Y).

rinputP2(X,Y):-
    member([X,empty],Y) -> updatep2(Y,X), inputP2(Y); write('Invalid input, try again: '), nl, inputP1(Y).

updatep2(Y,X):-
    delete(Y,[X,empty],L1), append([[X,circle]],L1,L2), printfield(L2), player2win(L2), player2tie(L2);
    delete(Y,[X,empty],L1), append([[X,cross]],L1,L2), inputP1(L2).



%check for win/tie Players
%check for player1
player1win(Y):-
    not(p1checkas(Y)), not(p1checkbs(Y)), not(p1checkcs(Y)),
    not(p1checkad(Y)), not(p1checkbd(Y)), not(p1checkcd(Y)),
    not(p1checkadi(Y)), not(p1checkcdi(Y)).

p1checkas(Y):-
    member([a1,cross],Y) -> member([a2,cross],Y) -> member([a3,cross],Y) -> nl, player1winmsg, end.
p1checkbs(Y):-
    member([b1,cross],Y) -> member([b2,cross],Y) -> member([b3,cross],Y) -> nl, player1winmsg, end.
p1checkcs(Y):-
    member([c1,cross],Y) -> member([c2,cross],Y) -> member([c3,cross],Y) -> nl, player1winmsg, end.
p1checkad(Y):-
    member([a1,cross],Y) -> member([b1,cross],Y) -> member([c1,cross],Y) -> nl, player1winmsg, end.
p1checkbd(Y):-
    member([a2,cross],Y) -> member([b2,cross],Y) -> member([c2,cross],Y) -> nl, player1winmsg, end.
p1checkcd(Y):-
    member([a3,cross],Y) -> member([b3,cross],Y) -> member([c3,cross],Y) -> nl, player1winmsg, end.
p1checkadi(Y):-
    member([a1,cross],Y) -> member([b2,cross],Y) -> member([c3,cross],Y) -> nl, player1winmsg, end.
p1checkcdi(Y):-
    member([a3,cross],Y) -> member([b2,cross],Y) -> member([c1,cross],Y) -> nl, player1winmsg, end.

player1winmsg:-
    write('Player1 wins!').

%check for player2
player2win(Y):-
    not(p2checkas(Y)), not(p2checkbs(Y)), not(p2checkcs(Y)),
    not(p2checkad(Y)), not(p2checkbd(Y)), not(p2checkcd(Y)),
    not(p2checkadi(Y)), not(p2checkcdi(Y)).

p2checkas(Y):-
    member([a1,circle],Y) -> member([a2,circle],Y) -> member([a3,circle],Y) -> nl, player2winmsg, end.
p2checkbs(Y):-
    member([b1,circle],Y) -> member([b2,circle],Y) -> member([b3,circle],Y) -> nl, player2winmsg, end.
p2checkcs(Y):-
    member([c1,circle],Y) -> member([c2,circle],Y) -> member([c3,circle],Y) -> nl, player2winmsg, end.
p2checkad(Y):-
    member([a1,circle],Y) -> member([b1,circle],Y) -> member([c1,circle],Y) -> nl, player2winmsg, end.
p2checkbd(Y):-
    member([a2,circle],Y) -> member([b2,circle],Y) -> member([c2,circle],Y) -> nl, player2winmsg, end.
p2checkcd(Y):-
    member([a3,circle],Y) -> member([b3,circle],Y) -> member([c3,circle],Y) -> nl, player2winmsg, end.
p2checkadi(Y):-
    member([a1,circle],Y) -> member([b2,circle],Y) -> member([c3,circle],Y) -> nl, player2winmsg, end.
p2checkcdi(Y):-
    member([a3,circle],Y) -> member([b2,circle],Y) -> member([c1,circle],Y) -> nl, player2winmsg, end.

player2winmsg:-
    write('Player2 wins!').

%check for tie
player1tie(Y):-
    member([_,empty],Y)-> inputP2(Y); printfield(Y), nl, tiemsg, end.
player2tie(Y):-
    member([_,empty],Y)-> inputP1(Y); printfield(Y), nl, tiemsg, end.



















